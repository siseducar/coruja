package modulos.secretaria.om;

import java.io.Serializable;

public class Contato implements Serializable {	
	
	private static final long serialVersionUID = 1L;
	
	/* Dados Pessoa Fisica */
	private Integer pkContato;
	private String telComercial;
	private String telResidencial;
	private String telCelular;
	private String email;
	
	/* Dados Pessoa Juridica */
	private String emailempresa;
	private String siteempresa;
	private String fax;
	private Integer status;
	private Boolean enviarNotificacao;
	
	public Integer getPkContato() {
		return pkContato;
	}
	public void setPkContato(Integer pkContato) {
		this.pkContato = pkContato;
	}
	public String getTelComercial() {
		return telComercial;
	}
	public void setTelComercial(String telComercial) {
		this.telComercial = telComercial;
	}
	public String getTelResidencial() {
		return telResidencial;
	}
	public void setTelResidencial(String telResidencial) {
		this.telResidencial = telResidencial;
	}
	public String getTelCelular() {
		return telCelular;
	}
	public void setTelCelular(String telCelular) {
		this.telCelular = telCelular;
	}
	
	public Boolean getEnviarNotificacao() {
		return enviarNotificacao;
	}
	public void setEnviarNotificacao(Boolean enviarNotificacao) {
		this.enviarNotificacao = enviarNotificacao;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getEmailempresa() {
		return emailempresa;
	}
	public void setEmailempresa(String emailempresa) {
		this.emailempresa = emailempresa;
	}
	public String getSiteempresa() {
		return siteempresa;
	}
	public void setSiteempresa(String siteempresa) {
		this.siteempresa = siteempresa;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
}
