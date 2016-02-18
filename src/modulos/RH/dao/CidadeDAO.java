package modulos.RH.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import modulos.RH.om.Cidade;
import modulos.sisEducar.conexaoBanco.ConectaBanco;
import modulos.sisEducar.dao.SisEducarDAO;
import modulos.sisEducar.utils.ConstantesSisEducar;

public class CidadeDAO extends SisEducarDAO
{
	// Realizando conexão com o banco
	ConectaBanco conexao = new ConectaBanco();
	Connection con = conexao.getConection();
	Statement st = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	/**
	 * O método busca o objeto cidade completo com estado e pais
	 * @author João Paulo
	 * @param sigla
	 * @param nome
	 * @return Cidade
	 * @throws SQLException
	 */
	public Cidade obtemCidade(String sigla, String nome) throws SQLException
	{
		Cidade cidade = null;
		Integer numeroArgumentos = 1;
		
		String querySQL = "SELECT * FROM cidade"
				+ " WHERE status = ?";
		
		if(sigla!=null && sigla.length() >0) { querySQL += " AND sigla = ?"; }
		if(nome!=null && nome.length() >0)	 { querySQL += " AND nome = ?"; }
		
		ps = con.prepareStatement(querySQL);
		
		ps.setInt(numeroArgumentos, ConstantesSisEducar.STATUS_ATIVO);
		
		if(sigla!=null && sigla.length()>0) 
		{ 
			numeroArgumentos ++; 
			ps.setString(numeroArgumentos, sigla);
		}
		
		if(nome!=null && nome.length()>0)	
		{ 
			numeroArgumentos ++; 
			ps.setString(numeroArgumentos, nome);
		}
		
		ResultSet rs = ps.executeQuery();
		if(rs.next())
		{
			cidade = new Cidade();
			cidade.setPkCidade(rs.getInt("pkcidade"));
			cidade.setNome(rs.getString("nome"));
			cidade.setSigla(rs.getString("sigla"));
			cidade.setCodigoibge(rs.getInt("codigoibge"));
			cidade.setStatus(rs.getInt("status"));
			cidade.setEstado(new EstadoDAO().obtemEstado(rs.getInt("fkstado"), null, null));
			
			return cidade;
		}
		
		return null;
	}
	
	/**
	 * O método busca a pk da cidade
	 * @author João Paulo
	 * @param sigla
	 * @param nome
	 * @return Cidade
	 * @throws SQLException
	 */
	public Integer obtemPKCidade(String sigla, String nome) throws SQLException
	{
		Integer numeroArgumentos = 1;
		
		String querySQL = "SELECT * FROM cidade"
				+ " WHERE status = ?";
		
		if(sigla!=null && sigla.length() >0) { querySQL += " AND sigla = ?"; }
		if(nome!=null && nome.length() >0)	 { querySQL += " AND nome = ?"; }
		
		ps = con.prepareStatement(querySQL);
		
		ps.setInt(numeroArgumentos, ConstantesSisEducar.STATUS_ATIVO);
		
		if(sigla!=null && sigla.length()>0) 
		{ 
			numeroArgumentos ++; 
			ps.setString(numeroArgumentos, sigla);
		}
		
		if(nome!=null && nome.length()>0)	
		{ 
			numeroArgumentos ++; 
			ps.setString(numeroArgumentos, nome);
		}
		
		ResultSet rs = ps.executeQuery();
		if(rs.next())
		{
			return rs.getInt("pkcidade");
		}
		
		return null;
	}
	
	/**
	 * O método busca a cidade de acordo com o estado selecionado na tela
	 * 
	 *  */
	public List<Cidade> consultaCidade(Integer pkEstado) throws SQLException{		
		
		List<Cidade> listaCidade = new ArrayList<Cidade>();
		String querySQL = "SELECT * FROM CIDADE WHERE FKESTADO = ? ORDER BY NOME";
		
		Statement stm = con.createStatement();
		ResultSet rs = stm.executeQuery(querySQL);
		
		while (rs.next()){
			Cidade paramCidade = new Cidade();
			paramCidade.setPkCidade(rs.getInt("PKCIDADE"));
			paramCidade.setNome(rs.getString("NOME"));
			paramCidade.setSigla(rs.getString("SIGLA"));
			
			listaCidade.add(paramCidade);
		}
		
		return listaCidade;
	}
}
