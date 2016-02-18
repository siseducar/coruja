package modulos.RH.om;

import java.sql.Date;


public class Usuario 
{
	/* Variaveis*/
	private String raAluno;
	private String cpfcnpj;
	private String pkUsuario;
	private String nome;
	private String senha;
	private String confirmarSenha;
	private Date dataLancamento;
	private String email;
	private String confirmarEmail;
	private int tipo;
	private int status;
	private String genero;
	private Pessoa pessoa;
	private Cidade cidadeOrigem;

	public Usuario() {}
	
	/* Getters and Setters */
	public String getNome() 				{ return nome; }
	public void setNome(String nome) 		{ this.nome = nome; }
	public String getSenha()				{ return senha; }
	public void setSenha(String senha) 		{ this.senha = senha; }
	public Date getDataLancamento() 		{ return dataLancamento; }
	public void setDataLancamento(Date dataLancamento) { this.dataLancamento = dataLancamento; }
	public String getEmail() 				{ return email; }
	public void setEmail(String email) 		{ this.email = email; }
	public int getTipo() 					{ return tipo; }
	public void setTipo(int tipo) 			{ this.tipo = tipo; }

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPkUsuario() {
		return pkUsuario;
	}

	public void setPkUsuario(String pkUsuario) {
		this.pkUsuario = pkUsuario;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}

	public String getCpfcnpj() {
		return cpfcnpj;
	}

	public void setCpfcnpj(String cpfcnpj) {
		this.cpfcnpj = cpfcnpj;
	}

	public String getRaAluno() {
		return raAluno;
	}

	public void setRaAluno(String raAluno) {
		this.raAluno = raAluno;
	}

	public String getConfirmarEmail() {
		return confirmarEmail;
	}

	public void setConfirmarEmail(String confirmarEmail) {
		this.confirmarEmail = confirmarEmail;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Cidade getCidadeOrigem() {
		return cidadeOrigem;
	}

	public void setCidadeOrigem(Cidade cidadeOrigem) {
		this.cidadeOrigem = cidadeOrigem;
	}
}
