package br.com.fiap.hidrosafe.excecoes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class TratadorExcecoes {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> naoEncontrado(RecursoNaoEncontradoException e) {
        return montar(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResposta> regraNegocio(RegraNegocioException e) {
        return montar(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> validacao(MethodArgumentNotValidException e) {
        StringBuilder mensagem = new StringBuilder();
        for (FieldError erro : e.getBindingResult().getFieldErrors()) {
            if (mensagem.length() > 0) {
                mensagem.append("; ");
            }
            mensagem.append(erro.getDefaultMessage());
        }
        return montar(HttpStatus.BAD_REQUEST, mensagem.toString());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> jsonInvalido(HttpMessageNotReadableException e) {
        return montar(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou mal formatado.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResposta> tipoInvalido(MethodArgumentTypeMismatchException e) {
        return montar(HttpStatus.BAD_REQUEST, "Valor inválido para o parâmetro '" + e.getName() + "'.");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResposta> metodoNaoPermitido(HttpRequestMethodNotSupportedException e) {
        return montar(HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP não permitido para este endereço.");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResposta> enderecoInexistente(NoResourceFoundException e) {
        return montar(HttpStatus.NOT_FOUND, "Endereço não encontrado.");
    }

    @ExceptionHandler(ErroBancoException.class)
    public ResponseEntity<ErroResposta> banco(ErroBancoException e) {
        int codigo = e.getCodigoErro();

        if (codigo == 1) {
            return montar(HttpStatus.CONFLICT, "Já existe um registro com esses dados.");
        }
        if (codigo == 2292) {
            return montar(HttpStatus.CONFLICT, "Não é possível excluir: o registro está sendo usado por outros dados.");
        }
        if (codigo == 2291) {
            return montar(HttpStatus.BAD_REQUEST, "Um dos registros relacionados informados não existe.");
        }
        if (codigo == 2290) {
            return montar(HttpStatus.BAD_REQUEST, "Valor inválido para um dos campos.");
        }
        return montar(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao acessar o banco de dados.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> generico(Exception e) {
        return montar(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor.");
    }

    private ResponseEntity<ErroResposta> montar(HttpStatus status, String mensagem) {
        ErroResposta erro = new ErroResposta(status.value(), status.getReasonPhrase(), mensagem);
        return ResponseEntity.status(status).body(erro);
    }
}
