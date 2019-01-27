package com.projeto.comercial.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.projeto.comercial.model.Oportunidade;
import com.projeto.comercial.repository.OportunidadeRepository;

// GET http://localhost:8080/oportunidades

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/oportunidades")
public class OportunidadeController {
	
	@Autowired
	OportunidadeRepository oportunidades;
	
	@GetMapping
	public List<Oportunidade> listar() {
		
		return oportunidades.findAll();
	}
	
	//ResponseEntity é usado para passar os códigos HTTP
	@GetMapping("/{id}")
	public ResponseEntity<Oportunidade> buscar(@PathVariable Long id) {
		Optional<Oportunidade> oportunidade = oportunidades.findById(id);
		
		//Na aula existia o método isEmpty(), porém aqui não apareceu
		if(!oportunidade.isPresent()) {
			//Not Found == 404
			return ResponseEntity.notFound().build();
		}
		
		//Ok == 200
		return ResponseEntity.ok(oportunidade.get());
	}
	
	
	/**A anotação Valid valida o objeto JSON enviado antes de salvar
	   A anotação ResponseBody é o que converte a resposta JSON em objeto Java**/
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Oportunidade adicionar(@Valid @RequestBody Oportunidade oportunidade) {
		
		Optional<Oportunidade> existente = oportunidades.findByDescricaoAndNomeProspecto(
				oportunidade.getDescricao(), oportunidade.getNomeProspecto());
		
			if(existente.isPresent()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Já existe uma oportunidade parecida");
				
			}
		
		return oportunidades.save(oportunidade);
	}
	
	@PostMapping("/excluir/")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void excluir(@RequestBody Oportunidade oportunidade) {
		
		if(oportunidades.findById(oportunidade.getId()).isPresent()) {
			oportunidades.deleteById(oportunidade.getId());
		}
	}
	
	@PostMapping("/atualizar/")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public Oportunidade atualizar (@RequestBody Oportunidade oportunidade) {
		
		return oportunidades.save(oportunidade);
	}

}
