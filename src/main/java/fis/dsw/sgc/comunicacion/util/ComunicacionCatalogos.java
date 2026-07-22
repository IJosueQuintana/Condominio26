package fis.dsw.sgc.comunicacion.util;

import java.util.Locale;

public final class ComunicacionCatalogos {
    private ComunicacionCatalogos() {}

    public static String codigoTipoMensaje(String etiqueta) {
        if (etiqueta == null) return "MENSAJE_GLOBAL";
        return switch (etiqueta) {
            case "Mensaje a residentes" -> "MENSAJE_RESIDENTES";
            case "Comunicado a trabajadores" -> "COMUNICADO_TRABAJADORES";
            case "Mensaje global" -> "MENSAJE_GLOBAL";
            case "Mensaje urgente" -> "MENSAJE_URGENTE";
            case "Alerta de emergencia" -> "ALERTA_EMERGENCIA";
            case "Boletín informativo" -> "BOLETIN_INFORMATIVO";
            default -> normalizar(etiqueta);
        };
    }

    public static String codigoTipoAnuncio(String etiqueta) {
        if (etiqueta == null) return "ANUNCIO_GENERAL";
        return switch (etiqueta) {
            case "Anuncio general" -> "ANUNCIO_GENERAL";
            case "Aviso de mantenimiento" -> "AVISO_MANTENIMIENTO";
            case "Boletín informativo" -> "BOLETIN_INFORMATIVO";
            case "Alerta de emergencia" -> "ALERTA_EMERGENCIA";
            default -> normalizar(etiqueta);
        };
    }

    public static String etiquetaTipo(String codigo) {
        if (codigo == null) return "Sin tipo";
        return switch (codigo.toUpperCase(Locale.ROOT)) {
            case "MENSAJE_RESIDENTES" -> "Mensaje a residentes";
            case "COMUNICADO_TRABAJADORES" -> "Comunicado a trabajadores";
            case "MENSAJE_GLOBAL" -> "Mensaje global";
            case "MENSAJE_URGENTE" -> "Mensaje urgente";
            case "ALERTA_EMERGENCIA" -> "Alerta de emergencia";
            case "BOLETIN_INFORMATIVO" -> "Boletín informativo";
            case "ANUNCIO_GENERAL" -> "Anuncio general";
            case "AVISO_MANTENIMIENTO" -> "Aviso de mantenimiento";
            case "MENSAJE" -> "Mensaje";
            case "ANUNCIO" -> "Anuncio";
            case "ALERTA" -> "Alerta";
            case "RECORDATORIO" -> "Recordatorio";
            case "SISTEMA" -> "Sistema";
            default -> codigo.replace('_', ' ');
        };
    }

    public static String codigoPrioridad(String etiqueta) {
        return etiqueta == null || etiqueta.isBlank() ? "NORMAL" : normalizar(etiqueta);
    }

    public static String etiquetaPrioridad(String codigo) {
        if (codigo == null || codigo.isBlank()) return "Normal";
        String n = codigo.toUpperCase(Locale.ROOT);
        return n.substring(0, 1) + n.substring(1).toLowerCase(Locale.ROOT);
    }

    public static String etiquetaEstado(String codigo) {
        if (codigo == null || codigo.isBlank()) return "Sin estado";
        return switch (codigo.toUpperCase(Locale.ROOT)) {
            case "BORRADOR" -> "Borrador";
            case "ENVIADO", "ENVIADA" -> "Enviada";
            case "PUBLICADO" -> "Publicado";
            case "LEIDA" -> "Leída";
            case "PENDIENTE" -> "Pendiente";
            case "FALLIDA" -> "Fallida";
            case "ELIMINADA" -> "Eliminada";
            case "CANCELADO", "CANCELADA" -> "Cancelada";
            case "EXPIRADO" -> "Expirado";
            default -> codigo.replace('_', ' ');
        };
    }

    public static String codigoEstadoNotificacion(String etiqueta) {
        if (etiqueta == null || etiqueta.equalsIgnoreCase("Todos")) return null;
        return normalizar(etiqueta);
    }

    public static String codigoTipoNotificacion(String etiqueta) {
        if (etiqueta == null || etiqueta.equalsIgnoreCase("Todas")) return null;
        return switch (etiqueta) {
            case "Mensaje" -> "MENSAJE";
            case "Anuncio" -> "ANUNCIO";
            case "Alerta" -> "ALERTA";
            case "Recordatorio" -> "RECORDATORIO";
            case "Sistema" -> "SISTEMA";
            default -> normalizar(etiqueta);
        };
    }

    private static String normalizar(String valor) {
        return valor.trim().toUpperCase(Locale.ROOT)
                .replace('Á','A').replace('É','E').replace('Í','I')
                .replace('Ó','O').replace('Ú','U').replace('Ñ','N')
                .replaceAll("[^A-Z0-9]+", "_").replaceAll("^_+|_+$", "");
    }
}
