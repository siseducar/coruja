package modulos.educacao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import modulos.educacao.om.Aluno;
import modulos.sisEducar.conexaoBanco.ConectaBanco;
import modulos.sisEducar.dao.SisEducarDAO;
import modulos.sisEducar.utils.ConstantesSisEducar;

public class AlunoDAO extends SisEducarDAO
{
	// Realizando conexão com o banco
	ConectaBanco conexao = new ConectaBanco();
	Connection con = conexao.getConection();
	Statement st = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	public AlunoDAO() throws SQLException {
		desabilitarAutoCommit(con);
	}
	
	/**
	 * Este método é usado para verificar a existência de um aluno no banco de dados, sendo buscado pelo próprio RA
	 * @param raAluno
	 * @return TRUE || FALSE
	 * @throws SQLException
	 */
	public Boolean verificaExistenciaAluno(String raAluno) throws SQLException
	{
		String querySQL = "SELECT p.pkPessoa, a.pkAluno, p.nome, a.ra FROM Pessoa p"
				+ " LEFT OUTER JOIN Aluno a ON(a.fkPessoa = p.pkPessoa)"
				+ " WHERE p.status = ?"
				+ " AND a.ra = ?";
		
		ps = con.prepareStatement(querySQL);
		
		ps.setInt(1 , ConstantesSisEducar.STATUS_ATIVO);
		ps.setString(2, raAluno);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) 
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Inseri um aluno
	 * @author João Paulo
	 * @param aluno
	 * @return Aluno
	 * @throws SQLException
	 */
	public Aluno inserirAluno(Aluno aluno) throws SQLException 
	{
		String querySQL = "INSERT INTO aluno "
				+ " ("
					+ " ra, ra2, folha, livro, registro, fkPessoa,"
					+ " fkcidadenascimento, livrouf, nomepai, nomemae"
				+ ") "
				+ " values(?,?,?,?,?,?,?,?,?,?)";
		ps = con.prepareStatement(querySQL);
		
		ps.setString(1, aluno.getRa());
		ps.setString(2, aluno.getRa2());
		ps.setString(3, aluno.getFolha());
		ps.setString(4, aluno.getLivro());
		ps.setInt(5, aluno.getRegistro()!=null ? aluno.getRegistro() : -1);
		ps.setInt(6, aluno.getPessoa().getPkPessoa());
		ps.setObject(7, aluno.getCidadeNascimento()!=null ? aluno.getCidadeNascimento().getPkCidade() : null);
		ps.setString(8, aluno.getLivroUF());
		
		fecharConexaoBanco(con, ps, false, true);
		
		aluno.setPkAluno(obtemPKAluno(aluno.getRa(), aluno.getRa2(), aluno.getRegistro()));
		
		return aluno;
	}
	
	/**
	 * Obtem a pk do aluno salvo pelos parâmeros passados
	 * @author João Paulo
	 * @param ra
	 * @param ra2
	 * @param registro
	 * @return PkAluno
	 * @throws SQLException
	 */
	public Integer obtemPKAluno(String ra, String ra2, Integer registro) throws SQLException
	{
		Integer numeroArgumentos = 1;
		
		String querySQL = "SELECT * FROM aluno"
				+ " WHERE ra = ?";
		
		if(ra2!=null && ra2.length() >0)		 	{ querySQL += " AND ra2 = ?"; }
		if(registro!=null && registro >0)	{ querySQL += " AND registro = ?"; }
		
		ps = con.prepareStatement(querySQL);
		
		ps.setString(numeroArgumentos, ra);
		
		if(ra2!=null && ra2.length()>0)	
		{ 
			numeroArgumentos ++; 
			ps.setString(numeroArgumentos, ra2);
		}
		
		if(registro!=null && registro>0)	
		{ 
			numeroArgumentos ++; 
			ps.setInt(numeroArgumentos, registro);
		}
				
		ResultSet rs = ps.executeQuery();
		if(rs.next())
		{
			return rs.getInt("pkAluno");
		}
		
		return null;
	}
	
	/**
	 * Metodo para salvar os dados referente ao cadastro de uma pessoa
	 * 
	 * */
	public Aluno salvarCadastroAluno(Aluno alunoDados) {
		try {
			String querySQL;
			
			Integer numeroArgumentos = 1;
			
			querySQL = " INSERT INTO ALUNO ( ";
			querySQL += " RA, RM, CODIGOINEP, FKREDEENSINO, FKUNIDADEESCOLAR, ";
			querySQL += " FKCURSO, FKETAPA, FKTURNO, ";
			if( alunoDados.getAlunoDeficiente() ) {
				querySQL += " FKTIPODEFICIENCIA, ";
			}
			
			querySQL += " FKPESSOA ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ";
			if( alunoDados.getAlunoDeficiente() ) {
				querySQL += " ,? ";
			}
			querySQL += " ? ) RETURNING PKALUNO ";
			
			ps = con.prepareStatement(querySQL);
			
			ps.setString(numeroArgumentos, alunoDados.getRa());
			numeroArgumentos++;
			
			ps.setString(numeroArgumentos, alunoDados.getRm());
			numeroArgumentos++;
			
			ps.setString(numeroArgumentos, alunoDados.getCodigoInep());
			numeroArgumentos++;
			
			ps.setInt(numeroArgumentos, alunoDados.getRedeEnsino().getPkRedeEnsino());
			numeroArgumentos++;
			
			ps.setInt(numeroArgumentos, alunoDados.getUnidadeEscolar().getPkUnidadeEscolar());
			numeroArgumentos++;
			
			ps.setInt(numeroArgumentos, alunoDados.getCurso().getPkCurso());
			numeroArgumentos++;
			
			ps.setInt(numeroArgumentos, alunoDados.getEtapa().getPkEtapa());
			numeroArgumentos++;
			
			ps.setInt(numeroArgumentos, alunoDados.getTurno().getPkTurno());
			numeroArgumentos++;
			
			if( alunoDados.getAlunoDeficiente() ) {
				ps.setInt(numeroArgumentos, alunoDados.getDeficiencia().getPkTipoDeficiencia());
				numeroArgumentos++;
			}
			
			if(rs.next()) {
				alunoDados.setPkAluno(rs.getInt("PKALUNO"));
			}
			
			fecharConexaoBanco(con, ps, true, false);
			
			return alunoDados;
		} catch (Exception e) {
			return null;
		}
	}
	
	/*
	 * Verifica cadastro aluno
	 * */
	public Boolean consultaAluno(String codigoRM) {
		try {
			String querySQL;
			
			querySQL = "SELECT * FROM ALUNO "
					+ " WHERE RM = ? ";
			
			ps = con.prepareStatement(querySQL);

			ps.setString(1, codigoRM);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				return true;
			}
			
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
