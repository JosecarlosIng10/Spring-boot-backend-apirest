package com.bolsadeideas.springboot.backend.apirest.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.services.IClienteService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class ClientesRestController {
	
	@Autowired
	private IClienteService clienteService;
	
	@GetMapping("/clientes")
	public List<Cliente> index(){
		return clienteService.findAll();
	}
	
	@GetMapping("/clientes/page/{page}")
	public Page<Cliente> index(@PathVariable Integer page){
		return clienteService.findAll(PageRequest.of(page, 4));
	}
	
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		Cliente cliente= null;
		Map<String,Object> responseMap = new HashMap<>();
		try {
			cliente = clienteService.findById(id);
		} catch (DataAccessException e) {
			responseMap.put("mensaje", "Error al realizar la consulta en la base de datos");
			return new ResponseEntity<Map<String,Object>>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (cliente==null) {
			responseMap.put("mensaje", "El cliente con el ID:".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(responseMap, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Cliente>(cliente,HttpStatus.OK);
	}
	
	@PostMapping("/clientes")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {
		Cliente clienteNew = null;
		Map<String,Object> responseMap = new HashMap<>();
		
		List<String> errors = new ArrayList<>();
		if (result.hasErrors()) {
			for (FieldError error: result.getFieldErrors()) {
				errors.add("El campo " + error.getField()+" "+ error.getDefaultMessage());
			}
			
			responseMap.put("errors", errors);
			return new ResponseEntity<Map<String,Object>>(responseMap, HttpStatus.BAD_REQUEST);
		}
		try {
			clienteNew = clienteService.save(cliente);
			
		} catch (DataAccessException e) {
			responseMap.put("mensaje", "Error al insertar en la base de datos");
			responseMap.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		responseMap.put("mensaje", "El cliente ha sido creado con exito");
		responseMap.put("cliente", clienteNew);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.CREATED);
	}
	
	@PutMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente,BindingResult result, @PathVariable Long id) {
		Cliente clienteActual = clienteService.findById(id);
		Cliente clienteUpdatedCliente = null;
		Map<String,Object> responseMap = new HashMap<>();
		
		List<String> errors = new ArrayList<>();
		
		if (result.hasErrors()) {
			for (FieldError error: result.getFieldErrors()) {
				errors.add("El campo " + error.getField()+" "+ error.getDefaultMessage());
			}
			
			responseMap.put("errors", errors);
			return new ResponseEntity<Map<String,Object>>(responseMap, HttpStatus.BAD_REQUEST);
		}
		
		if (clienteActual==null) {
			responseMap.put("mensaje", "El cliente con el ID:".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(responseMap, HttpStatus.NOT_FOUND);
		}
		
		try {
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setEmail(cliente.getEmail());
			clienteUpdatedCliente = clienteService.save(clienteActual);
		} catch (DataAccessException e) {
			responseMap.put("mensaje", "Error al actualizar en la base de datos");
			responseMap.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		responseMap.put("mensaje", "El cliente ha sido actualizado con exito");
		responseMap.put("cliente", clienteUpdatedCliente);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.CREATED);
		
	}
	
	@DeleteMapping("clientes/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String,Object> responseMap = new HashMap<>();
		try {
			 clienteService.delete(id);
		} catch (DataAccessException e) {
			responseMap.put("mensaje", "Error al eliminar el cliente en la base de datos");
			responseMap.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		responseMap.put("Mensaje", "El cliente ha sido eliminado con exito");
		return new ResponseEntity<Map<String,Object>>(responseMap,HttpStatus.OK);
	}
	
	@PostMapping("/clientes//upload")
	public ResponseEntity<?> upload(@RequestParam("arvhivo") MultipartFile archivoFile, @RequestParam("id") Long id){
		Map<String,Object> responseMap = new HashMap<>();
		
		Cliente cliente = clienteService.findById(id);
		
		if(!archivoFile.isEmpty()) {
			String nombreArchString = archivoFile.getOriginalFilename();
			Path rutaArchPath = Paths.get("uploads").resolve(nombreArchString).toAbsolutePath();
			
			
			try {
				Files.copy(archivoFile.getInputStream(), rutaArchPath);
			} catch (IOException e) {
				responseMap.put("mensaje", "Error al agregar imagen al cliente en la base de datos");
				responseMap.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String,Object>>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			cliente.setFoto(nombreArchString);
			
			clienteService.save(cliente);
			
			responseMap.put("Cliente", cliente);
			responseMap.put("mensaje", "Has subido correctamente la imagen: " + nombreArchString);
			
		}
		
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.CREATED);
	}
	

}
