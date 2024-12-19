/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Usuarios.service;

import com.Usuarios.model.Usuario;
import com.Usuarios.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author casti
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    /**
     * Función para listar todos los usuarios
     * @return Lista de usuarios
     */
    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    /**
     * Función para guardar los datos de un usuario
     * @param usuario Objeto Usuario a guardar
     */
    public void guardar(Usuario usuario) {
        repository.save(usuario);
    }

    /**
     * Función para buscar un usuario por su ID
     * @param id ID del usuario
     * @return Optional del usuario encontrado
     */
    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id);
    }

    /**
     * Función para eliminar el registro de un usuario
     * @param id ID del usuario a eliminar
     */
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}