package modulos.secretaria.services;

import modulos.secretaria.dao.EnderecoDAO;
import modulos.secretaria.om.Endereco;

public class EnderecoService {

	/**
	 * Salva os dados do endereco da pessoa
	 * @author Michael
	 * @param Endereco enderecoDados
	 * @return false/true
	 */
	public Boolean salvarDadosEndereco(Endereco enderecoDados) {
		try {
			enderecoDados = new EnderecoDAO().salvarEnderecoPessoa(enderecoDados);
			
			if( enderecoDados != null && enderecoDados.getPkEndereco() != null) {
				return true;
			}
			
		} catch (Exception e) {
			return false;
		}	
		
		return false;
	}
	
	/**
	 * Atualiza os dados do endereco da pessoa
	 * @author Michael
	 * @param Endereco enderecoDados
	 * @return false/true
	 */
	public Boolean atualizarDadosEndereco(Endereco enderecoDados) {
		try {
			if( new EnderecoDAO().atualizarEndercoPessoa(enderecoDados)) {
				return true;
			} else {
				return false;
			}
			
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Consulta os dados do endereco da pessoa
	 * @author Michael
	 * @param Endereco enderecoDados
	 */
	public Endereco consultaDadosEndereco(Integer pkEndereco) {
		try {
			Endereco enderecoDados = new Endereco();
			
			enderecoDados = new EnderecoDAO().consultaEnderecoPessoa(pkEndereco);
			
			return enderecoDados;
		} catch (Exception e) {
			return null;
		}
	}
}
