package UI.src.ui.utils;

import javax.swing.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Util {
    public static void validarCampo(JTextField campo, String nombreCampo, int maxLongitud) {
        String texto = campo.getText().trim();

        String nombreLimpio = texto.replaceAll("[^0-9]", "");

        if(!nombreLimpio.isEmpty()){
            throw new IllegalArgumentException(nombreCampo + " no puede tener caracteres numéricos");
        }

        if (texto.isEmpty()) {
            throw new IllegalArgumentException(nombreCampo + " no puede estar vacío");
        }

        if (texto.length() > maxLongitud) {
            throw new IllegalArgumentException(nombreCampo + " no puede tener más de " + maxLongitud + " caracteres");
        }

        if(!texto.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]+$")){
            throw new IllegalArgumentException(nombreCampo + " no puede tener caracteres especiales");
        }
    }

    // Validar teléfono
    public static void validarTelefono(String telefono) {
        if (telefono.isEmpty()) {
            throw new IllegalArgumentException("Teléfono no puede estar vacío");
        }

        // Remover espacios, guiones, paréntesis, etc.
        String numeroLimpio = telefono.replaceAll("[^0-9]", "");

        if (!numeroLimpio.matches("\\d+")) {
            throw new IllegalArgumentException("Teléfono debe contener solo números");
        }

        if(!numeroLimpio.equals(telefono)){
            throw new IllegalArgumentException("Teléfono solo puede contener caracteres numéricos");
        }

        if (numeroLimpio.length() > 9 || numeroLimpio.length() < 8) {
            throw new IllegalArgumentException("Teléfono debe tener máximo 9 dígitos y minimo 7");
        }
    }

    // Validar email
    public static void validarEmail(String email) {
        if (email.isEmpty()) {
            throw new IllegalArgumentException("Email no puede estar vacío");
        }

        // Expresión regular para validar email
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Email no tiene un formato válido");
        }
    }

    public static String detectarCampoDuplicado(SQLException e) {
        String mensaje = e.getMessage();


        if (mensaje.contains("nombre_index")) {
            return "El nombre ya existe en la base de datos. Por favor, elija otro nombre.";
        }
        else if (mensaje.contains("email_index")) {
            return "El correo electrónico ya está registrado.";
        }
        else {
            return "Registro duplicado: " + mensaje;
        }
    }
    
    public static String formatFecha(Date fecha) {
            // Formato de salida
            SimpleDateFormat formatterSalida = new SimpleDateFormat("dd/MM/yyyy");

            // Formatear la fecha
            return formatterSalida.format(fecha);
    }

    public static java.sql.Date convertirFecha(java.util.Date fechaUtil) {
        if (fechaUtil == null) {
            return null;
        }
        fechaUtil.setTime(fechaUtil.getTime() - fechaUtil.getTime() % (24 * 60 * 60 * 1000));
        return new java.sql.Date(fechaUtil.getTime());
    }
}
