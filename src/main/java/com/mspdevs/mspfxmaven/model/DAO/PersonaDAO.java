package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.Persona;

import javafx.collections.ObservableList;

public interface PersonaDAO<Persona> {
    ObservableList<Persona> listarTodos() throws Exception;
}