package modulos.RH.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import modulos.RH.om.Usuario;
import modulos.sisEducar.conexaoBanco.ConectaBanco;
import modulos.sisEducar.dao.SisEducarDAO;
import modulos.sisEducar.utils.ConstantesSisEducar;

public class UsuarioDAO extends SisEducarDAO
{
	// Realizando conex�o com o banco
	ConectaBanco conexao = new ConectaBanco();
	Connection con = conexao.getConection();
	Statement st = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	Date dataAtual = new Date(System.currentTimeMillis());
	
	public UsuarioDAO() throws SQLException
	{
		//Quando construir a classe, dever� desabilitar o autoCommit
		desabilitarAutoCommit(con);
	}
	
	/**
	 * Faz uma busca no banco para verificar se existe um usuario com o mesmo nome que o usuario esta tentando cadastrar
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	public Boolean existeUsuario(Usuario usuario) throws SQLException
	{
		String querySQL = "SELECT * FROM Usuario WHERE nome = ? AND status = ?";
		
		ps = con.prepareStatement(querySQL);
		
		ps.setString(1, usuario.getNome());
		ps.setInt(2 , ConstantesSisEducar.STATUS_ATIVO);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) 
		{
			fecharConexaoBanco(con, ps, true, false);
			return true;
		}
		
		fecharConexaoBanco(con, ps, true, false);
		return false;
	}
	
	/**
	 * O m�todo � usado para inserir um novo usu�rio no banco de dados
	 * @param loginBean
	 * @return
	 * @throws SQLException 
	 */
	public Boolean inserirUsuario(Usuario usuario) throws SQLException 
	{
		String querySQL = "INSERT INTO usuario "
				+ " (nome, senha, dataLancamento,  tipo, email, status, cpfcnpj) values(?,?,?,?,?,?,?)";
		ps = con.prepareStatement(querySQL);
		
		ps.setString(1, usuario.getNome());
		ps.setString(2, usuario.getSenha());
		ps.setDate(3, dataAtual);
		ps.setInt(4, usuario.getTipo());
		ps.setString(5, usuario.getEmail());
		ps.setInt(6, ConstantesSisEducar.STATUS_INCOMPLETO);
		ps.setString(7, usuario.getCpfcnpj());
		
		//Depois que terminar o cadastro de pessoa, remover esta linha e adicionar corretamente a pessoa
		
		fecharConexaoBanco(con, ps, true, true);
		
		return true;
	}
	
	/**
	 * O m�todo � usado para consultar o banco e verificar se existe o usu
	 * 
	 * @param loginBean
	 * @return
	 * @throws SQLException 
	 */
	public Boolean validarUsuario(Usuario usuario) throws SQLException
	{
		String querySQL = "SELECT * FROM usuario "
				+ " WHERE nome = ?"
				+ " AND senha = ?"
				+ " AND status = ?";
		ps = con.prepareStatement(querySQL);
		
		ps.setString(1, usuario.getNome());
		ps.setString(2, usuario.getSenha());
		ps.setInt(3, ConstantesSisEducar.STATUS_ATIVO);
		
		ResultSet rs = ps.executeQuery();
		if(rs.next())
		{
			usuario.setNome(rs.getString("nome"));
			usuario.setSenha(rs.getString("senha"));
			
			fecharConexaoBanco(con, ps, true, false);
			return true;
		}
		
		fecharConexaoBanco(con, ps, true, false);
		return false;
	}
	
	/**
	 * O m�todo � usado para buscar a existencia do usu�rio pelo cpf do mesmo
	 * @param cpf
	 * @return TRUE || FALSE
	 * @throws SQLException
	 */
	public Boolean verificaExistenciaResponsavel(String cpfcnpj) throws SQLException
	{
		String querySQL = "SELECT r.pkResponsavel, p.pkPessoa, a.pkAluno, " +
								" CASE WHEN p.nome IS NOT NULL THEN p.nome" +
								" ELSE p.nomefantasia" +
								" END as pessoaNome" +
							" FROM Responsavel r" +
							" INNER JOIN Pessoa p ON(r.fkPessoa = p.pkPessoa)" +
							" INNER JOIN Aluno a ON(r.fkAluno = a.pkAluno)" +
							" WHERE p.status = ?" +
							" AND p.cpf = ? OR p.cnpj = ?";
		
		ps = con.prepareStatement(querySQL);
		
		ps.setInt(1 , ConstantesSisEducar.STATUS_ATIVO);
		ps.setString(2, cpfcnpj);
		ps.setString(3, cpfcnpj);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) 
		{
			fecharConexaoBanco(con, ps, true, false);
			return true;
		}
		
		fecharConexaoBanco(con, ps, true, false);
		return false;
	}
	
	/**
	 * M�todo usado para obter um usuario, pode ser adicionado mais parametros neste m�todo, so deve ser verificado aonde esta usando este metodo
	 * para que nas chamadas do mesmo sejam passados os novos parametros
	 * @param email
	 * @param status
	 * @return {@link} Usuario
	 * @throws SQLException
	 */
	public Usuario obtemUsuario(String email, Integer status) throws SQLException
	{
		Usuario usuario = new Usuario();
		String querySQL = "SELECT * FROM Usuario "
				+ " WHERE email = ?"
				+ " AND status = ?";
		
		ps = con.prepareStatement(querySQL);
		
		ps.setString(1, email);
		ps.setInt(2 , status);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) 
		{
			usuario.setPkUsuario(rs.getString("pkUsuario"));
			usuario.setNome(rs.getString("nome"));
			usuario.setSenha(rs.getString("senha"));
			usuario.setEmail(rs.getString("email"));
			usuario.setDataLancamento(rs.getDate("dataLancamento"));
			usuario.setCpfcnpj(rs.getString("cpfcnpj"));
			usuario.setStatus(rs.getInt("status"));
			usuario.setTipo(rs.getInt("tipo"));
			
			fecharConexaoBanco(con, ps, true, false);
			return usuario;
		}
		
		fecharConexaoBanco(con, ps, true, false);
		return null;
	}
	
	/**
	 * M�todo usado para atualizar o usuario que esta com o status incompleto
	 * @param pkUsuario
	 * @return TRUE || FALSE
	 * @throws SQLException
	 */
	public Boolean atualizarUsuarioIncompleto(String pkUsuario) throws SQLException
	{
		String querySQL = "UPDATE Usuario "
				+ " SET status = ?"
				+ " WHERE pkUsuario = ?";
		
		ps = con.prepareStatement(querySQL);
		
		ps.setInt(1, ConstantesSisEducar.STATUS_ATIVO);
		ps.setInt(2 , Integer.parseInt(pkUsuario));
		ps.execute();
		
		fecharConexaoBanco(con, ps, true, true);

		return true;
	}
	
	/**
	 * O m�todo � usado para atualizar a senha do usuario
	 * @param pkUsuario
	 * @return TRUE || FALSE
	 * @throws SQLException
	 */
	public Boolean redefinirSenha(String pkUsuario, String novaSenha) throws SQLException
	{
		String querySQL = "UPDATE Usuario "
				+ " SET senha = ?"
				+ " WHERE pkUsuario = ?";
		
		ps = con.prepareStatement(querySQL);
		
		ps.setString(1, novaSenha);
		ps.setInt(2 , Integer.parseInt(pkUsuario));
		ps.execute();
		
		fecharConexaoBanco(con, ps, true, true);
		
		return true;
	}
}