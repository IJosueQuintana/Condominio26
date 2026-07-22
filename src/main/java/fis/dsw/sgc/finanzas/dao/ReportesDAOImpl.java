package fis.dsw.sgc.finanzas.dao;

import fis.dsw.sgc.conexion_bd.DBConnection;
import fis.dsw.sgc.finanzas.dto.DetalleGastoDTO;
import fis.dsw.sgc.finanzas.dto.DetallePagoDTO;
import fis.dsw.sgc.finanzas.dto.ReporteRendicionDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportesDAOImpl implements IReportesDAO {

    @Override
    public List<DetalleGastoDTO> buscarGastosPorRangoFechas(LocalDate inicio, LocalDate fin) {
        List<DetalleGastoDTO> lista = new ArrayList<>();
        // Agregamos fecha_gasto a la consulta SQL
        String sql = "SELECT tipo_gasto, descripcion, valor_centavos, fecha_gasto FROM gasto " +
                "WHERE fecha_gasto BETWEEN ? AND ? AND estado = 'REGISTRADO'";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inicio.toString());
            pstmt.setString(2, fin.toString());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String motivo = rs.getString("tipo_gasto");
                String desc = rs.getString("descripcion");
                double valor = rs.getInt("valor_centavos") / 100.0;
                LocalDate fecha = LocalDate.parse(rs.getString("fecha_gasto")); // Capturamos la fecha

                lista.add(new DetalleGastoDTO(motivo, desc, valor, fecha));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar gastos para el reporte.", e);
        }
        return lista;
    }

    @Override
    public List<DetallePagoDTO> buscarPagosPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return ejecutarConsultaPagos(inicio, fin, null);
    }

    @Override
    public List<DetallePagoDTO> buscarPagosPorRangoFechasYUsuario(LocalDate inicio, LocalDate fin, int idUsuario) {
        return ejecutarConsultaPagos(inicio, fin, idUsuario);
    }

    private List<DetallePagoDTO> ejecutarConsultaPagos(LocalDate inicio, LocalDate fin, Integer idUsuario) {
        List<DetallePagoDTO> lista = new ArrayList<>();

        // Agregamos p.fecha_pago a la consulta SQL
        StringBuilder sql = new StringBuilder(
                "SELECT u.numero_cedula as cedula, td.codigo as motivo, p.valor_pagado_centavos as valor, p.fecha_pago as fecha " +
                        "FROM pago p " +
                        "JOIN deuda d ON p.id_deuda = d.id_deuda " +
                        "JOIN usuario u ON d.id_usuario = u.id_usuario " +
                        "JOIN tipo_deuda td ON d.id_tipo_deuda = td.id_tipo_deuda " +
                        "WHERE DATE(p.fecha_pago) BETWEEN ? AND ? AND p.estado = 'VALIDADO'"
        );

        if (idUsuario != null) {
            sql.append(" AND d.id_usuario = ?");
        }

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            pstmt.setString(1, inicio.toString());
            pstmt.setString(2, fin.toString());

            if (idUsuario != null) {
                pstmt.setInt(3, idUsuario);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String cedula = rs.getString("cedula");
                String motivo = rs.getString("motivo");
                double valor = rs.getInt("valor") / 100.0;

                // La fecha de pago es TIMESTAMP en tu BD, así que extraemos solo la parte de la fecha (YYYY-MM-DD)
                String fechaCompleta = rs.getString("fecha");
                LocalDate fecha = LocalDate.parse(fechaCompleta.split(" ")[0]);

                lista.add(new DetallePagoDTO(cedula, motivo, valor, fecha));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar pagos para el reporte.", e);
        }
        return lista;
    }

    @Override
    public void guardarReporteRendicion(ReporteRendicionDTO dto) {
        String sql = "INSERT INTO reporte_rendicion (fecha_inicio, fecha_fin, total_servicios_centavos, " +
                "total_sueldos_centavos, total_otros_centavos, total_gastos_centavos, total_multas_centavos, " +
                "total_alicuotas_centavos, total_reservas_centavos, total_ingresos_centavos, balance_neto_centavos, observaciones) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dto.fechaInicio);
            pstmt.setString(2, dto.fechaFin);
            pstmt.setInt(3, (int) Math.round(dto.totalServiciosBasicos * 100));
            pstmt.setInt(4, (int) Math.round(dto.totalSueldosGastos * 100));
            pstmt.setInt(5, (int) Math.round(dto.totalOtrosGastos * 100));
            pstmt.setInt(6, (int) Math.round(dto.totalGastosGeneral * 100));
            pstmt.setInt(7, (int) Math.round(dto.totalMultas * 100));
            pstmt.setInt(8, (int) Math.round(dto.totalAlicuotas * 100));
            pstmt.setInt(9, (int) Math.round(dto.totalReservas * 100));
            pstmt.setInt(10, (int) Math.round(dto.totalIngresosGeneral * 100));
            pstmt.setInt(11, (int) Math.round(dto.balanceNeto * 100));
            pstmt.setString(12, dto.observaciones);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el reporte de rendición de cuentas.", e);
        }
    }

    @Override
    public ReporteRendicionDTO buscarReporteRendicion(LocalDate inicio, LocalDate fin) {
        String sql = "SELECT * FROM reporte_rendicion WHERE fecha_inicio = ? AND fecha_fin = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inicio.toString());
            pstmt.setString(2, fin.toString());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                ReporteRendicionDTO dto = new ReporteRendicionDTO();
                dto.fechaInicio = rs.getString("fecha_inicio");
                dto.fechaFin = rs.getString("fecha_fin");
                dto.totalServiciosBasicos = rs.getInt("total_servicios_centavos") / 100.0;
                dto.totalSueldosGastos = rs.getInt("total_sueldos_centavos") / 100.0;
                dto.totalOtrosGastos = rs.getInt("total_otros_centavos") / 100.0;
                dto.totalGastosGeneral = rs.getInt("total_gastos_centavos") / 100.0;
                dto.totalMultas = rs.getInt("total_multas_centavos") / 100.0;
                dto.totalAlicuotas = rs.getInt("total_alicuotas_centavos") / 100.0;
                dto.totalReservas = rs.getInt("total_reservas_centavos") / 100.0;
                dto.totalIngresosGeneral = rs.getInt("total_ingresos_centavos") / 100.0;
                dto.balanceNeto = rs.getInt("balance_neto_centavos") / 100.0;
                dto.observaciones = rs.getString("observaciones");
                return dto;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el reporte.", e);
        }
        return null;
    }

    @Override
    public boolean existeReporteRendicion(LocalDate inicio, LocalDate fin) {
        String sql = "SELECT 1 FROM reporte_rendicion WHERE fecha_inicio = ? AND fecha_fin = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inicio.toString());
            pstmt.setString(2, fin.toString());
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Retorna true si hay al menos un registro
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia del reporte.", e);
        }
    }


}