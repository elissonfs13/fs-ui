package com.futebolsimulador.service;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futebolsimulador.model.entity.Grupo;
import com.futebolsimulador.model.entity.InfoSelecaoNoGrupo;
import com.futebolsimulador.model.entity.Selecao;
import com.futebolsimulador.repository.GrupoRepository;

@Service
public class GrupoService {
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private JogoService jogoService;
	
	public ArrayList<Grupo> geraGrupos(ArrayList<Selecao> selecoes){
		ArrayList<Grupo> grupos = new ArrayList<Grupo>();
		String[] nomeGrupos = {"A", "B", "C", "D", "E", "F", "G", "H"};
		int num = 0;
		for (int i = 0; i < 8; i++){
			grupos.add(novoGrupo(nomeGrupos[i], selecoes.get(num), selecoes.get(num+1), selecoes.get(num+2), selecoes.get(num+3)));
			num = num + 4;
		}
		return grupos;
	}
	
	public Grupo novoGrupo(String nome, Selecao sel1, Selecao sel2, Selecao sel3, Selecao sel4){
		Boolean empate = Boolean.TRUE;
		Grupo grupo = new Grupo(nome);
		ArrayList<InfoSelecaoNoGrupo> infoSelecoes = getInfosSels(sel1, sel2, sel3, sel4);
		grupo.setJogos(jogoService.getJogosGrupo(empate, infoSelecoes));
		calculaSaldoGols(infoSelecoes);
		calculaClassificacao(infoSelecoes);
		grupo.setInfoSelecoes(infoSelecoes);
		//grupoRepository.save(grupo); testar se quando salvar o campeonato ja salva o grupo
		return grupo;
	}

	

	private void calculaClassificacao(ArrayList<InfoSelecaoNoGrupo> infoSelecoes) {
		Collections.sort(infoSelecoes);
		int posicao = 1;
		for (InfoSelecaoNoGrupo infoSelecao : infoSelecoes){
			infoSelecao.setClassificacao(new Integer(posicao));
			posicao++;
		}
	}

	private void calculaSaldoGols(ArrayList<InfoSelecaoNoGrupo> infoSelecoes) {
		for (InfoSelecaoNoGrupo infoSelecao : infoSelecoes){
			infoSelecao.setSaldoGols(infoSelecao.getGolsMarcados() - infoSelecao.getGolsSofridos());
		}
	}

	private ArrayList<InfoSelecaoNoGrupo> getInfosSels(Selecao sel1, Selecao sel2, Selecao sel3, Selecao sel4) {
		ArrayList<InfoSelecaoNoGrupo> infoSels = new ArrayList<InfoSelecaoNoGrupo>();
		infoSels.add(new InfoSelecaoNoGrupo(sel1));
		infoSels.add(new InfoSelecaoNoGrupo(sel2));
		infoSels.add(new InfoSelecaoNoGrupo(sel3));
		infoSels.add(new InfoSelecaoNoGrupo(sel4));
		return infoSels;
	}

}