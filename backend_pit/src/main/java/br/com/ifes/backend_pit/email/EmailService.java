package br.com.ifes.backend_pit.email;

public interface EmailService {
    void enviarEmail(
            String para, String assunto, String mensagem);
}
