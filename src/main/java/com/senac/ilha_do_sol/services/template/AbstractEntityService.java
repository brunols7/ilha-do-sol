package com.senac.ilha_do_sol.services.template;

public abstract class AbstractEntityService<T> {

    public final T criar(T entidade) {
        validarCamposObrigatorios(entidade);
        validarRegrasNegocio(entidade);
        T entidadeCriada = salvar(entidade);
        executarPosProcessamento(entidadeCriada);
        return entidadeCriada;
    }

    public final T atualizar(Long id, T entidadeAtualizada) {
        T entidadeExistente = buscarPorId(id);
        validarExistencia(entidadeExistente);
        aplicarAtualizacoes(entidadeExistente, entidadeAtualizada);
        validarRegrasNegocio(entidadeExistente);
        return salvar(entidadeExistente);
    }

    protected abstract void validarCamposObrigatorios(T entidade);

    protected abstract void validarRegrasNegocio(T entidade);

    protected abstract T salvar(T entidade);

    protected abstract T buscarPorId(Long id);

    protected abstract void aplicarAtualizacoes(T entidadeExistente, T entidadeAtualizada);

    protected void validarExistencia(T entidade) {
        if (entidade == null) {
            throw new IllegalArgumentException("Entidade não encontrada");
        }
    }

    protected void executarPosProcessamento(T entidade) {
    }
}
