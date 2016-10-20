package modulos.educacao.servlet;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;

import modulos.educacao.dao.HorarioDAO;
import modulos.educacao.om.Horario;
import modulos.educacao.om.HorarioAula;
import modulos.secretaria.dao.UnidadeEscolarDAO;
import modulos.secretaria.om.Turno;
import modulos.secretaria.om.UnidadeEscolar;
import modulos.sisEducar.utils.Logs;

@ManagedBean(name="horarioServlet")
@ViewScoped
public class HorarioServlet implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String codigoUnidadeEscolar;
	private String nomeUnidadeEscolar;
	private Turno turnoDado;
	private Horario horario;
	private Boolean btRemoverEnabled = false;
	private UnidadeEscolar unidadeEscolarSelecionada = null;
	
	private List<HorarioAula> aulas;
	
	public HorarioServlet()
	{
		aulas = new ArrayList<HorarioAula>();
		btRemoverEnabled = false;
		
		if(horario==null) { horario = new Horario(); }
		if(turnoDado==null) { turnoDado = new Turno(); }
	}
	
	public void cadastrarHorario()
	{
		try 
		{
			Boolean resultado = false;
			if(unidadeEscolarSelecionada!=null && turnoDado!=null)
			{
				horario.setUnidadeEscolar(unidadeEscolarSelecionada);
				horario.setTurno(turnoDado);
				horario.setHorariosAula(aulas);
				
				if(horario.getPkHorario()!=null)
				{
					new HorarioDAO().deletarHorariosAula(horario);
					resultado = new HorarioDAO().editarHorario(horario);
				}
				else
				{
					resultado = new HorarioDAO().inserirHorario(horario);
				}
				
				if(resultado)
				{
					limparFormulario();
					if(horario.getPkHorario()!=null)
					{
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O Horário foi editado com sucesso", null));
					}
					else
					{
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O Horário foi salvo com sucesso", null));
					}
				}
				else
				{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O Horário não foi salvo", null));
				}
			}
		} 
		catch (Exception e) 
		{
			Logs.addError("cadastrarHorario", "cadastrarHorario");
		}
		
	}
	
	public void removerHorario()
	{
		List<HorarioAula> aulasAux = new ArrayList<HorarioAula>();
		HorarioAula aulaSelecionada = null;
		
		if(dataTableAulas!=null && dataTableAulas.getRowData()!=null) { aulaSelecionada = (HorarioAula) dataTableAulas.getRowData(); }
		
		if(aulaSelecionada != null && aulaSelecionada.getInicioAux()!=null && aulaSelecionada.getTerminoAux()!=null)
		{
			for (HorarioAula aula : aulas) 
			{
				if(!aula.getInicioAux().equals(aulaSelecionada.getInicioAux()) && !aula.getTerminoAux().equals(aulaSelecionada.getTerminoAux()))
				{
					aulasAux.add(aula);
				}
			}
			
			aulas = aulasAux;
		}
	}
	
	/**
	 * Adiciona os horários de acordo com preenchimento de informações do usuário na tela
	 * @author João Paulo
	 * @return
	 */
	public void adicionarAula()
	{
		try
		{
			String inicioHora = null;
			String inicioMinuto = null;
			String terminoHora = null;
			String terminoMinuto = null;
			String horaAulaHora = null;
			String horaAulaMinuto = null;
			Double inicio = null;
			Double termino = null;
			Double horaAula = null;
			Integer minutosIniciais = 0;
			Integer minutosFinais = 0;
			HorarioAula aula = null;
			String horarioConvertido = "";
			
			if(aulas.size()>0)
			{
				inicioHora = aulas.get(aulas.size()-1).getTerminoAux().substring(0, 2);
				inicioMinuto = aulas.get(aulas.size()-1).getTerminoAux().substring(3, 5);
				
				terminoHora = horario.getTerminoAux().substring(0, 2);
				terminoMinuto = horario.getTerminoAux().substring(2, 4);
			}
			else
			{
				inicioHora = horario.getInicioAux().substring(0, 2);
				inicioMinuto = horario.getInicioAux().substring(2, horario.getInicioAux().length());
				
				terminoHora = horario.getTerminoAux().substring(0, 2);
				terminoMinuto = horario.getTerminoAux().substring(2, horario.getTerminoAux().length());
			}
			
			horaAulaHora = horario.getHoraAulaAux().substring(0, 2);
			horaAulaMinuto = horario.getHoraAulaAux().substring(2, horario.getHoraAulaAux().length());
			
			inicio = new Double(inicioHora + "." + inicioMinuto);
			termino = new Double(terminoHora + "." + terminoMinuto);
			horaAula = new Double(horaAulaHora + "." + horaAulaMinuto);
			
			minutosIniciais = (new Integer(inicioHora) * 60) + (new Integer(inicioMinuto));
			minutosFinais = (new Integer(terminoHora) * 60) + (new Integer(terminoMinuto));
			
			minutosIniciais += ((new Integer(horaAulaHora) * 60) + (new Integer(horaAulaMinuto)));
			if(minutosIniciais<=minutosFinais)
			{
				horarioConvertido = converterHoraMinuto(new Integer(inicioHora), new Integer(inicioMinuto), new Integer(horaAulaHora), new Integer(horaAulaMinuto), 0, 0);
				
				aula = preencherOMHorarioAula(inicioHora, inicioMinuto, horarioConvertido, false);
				aula.setTipoIntervalo(false);
				aulas.add(aula);
			}
		} 
		catch (Exception e) 
		{
			Logs.addError("adicionarAula", "adicionarAula");
		}
	}
	
	public void adicionarIntervalo()
	{
		try
		{
			String inicioHora = null;
			String inicioMinuto = null;
			String terminoHora = null;
			String terminoMinuto = null;
			String intervaloHora = null;
			String intervaloMinuto = null;
			String horaAulaHora = null;
			String horaAulaMinuto = null;
			Double inicio = null;
			Double termino = null;
			Integer minutosIniciais = 0;
			Integer minutosFinais = 0;
			HorarioAula aula = null;
			String horarioConvertido = "";
			
			if(aulas.size()>0)
			{
				inicioHora = aulas.get(aulas.size()-1).getTerminoAux().substring(0, 2);
				inicioMinuto = aulas.get(aulas.size()-1).getTerminoAux().substring(3, 5);
				
				terminoHora = horario.getTerminoAux().substring(0, 2);
				terminoMinuto = horario.getTerminoAux().substring(2, 4);
				
				intervaloHora = horario.getIntervaloAux().substring(0, 2);
				intervaloMinuto = horario.getIntervaloAux().substring(2, 4);
				
				inicio = new Double(inicioHora + "." + inicioMinuto);
				termino = new Double(terminoHora + "." + terminoMinuto);
				
				minutosIniciais = (new Integer(inicioHora) * 60) + (new Integer(inicioMinuto));
				minutosFinais = (new Integer(terminoHora) * 60) + (new Integer(terminoMinuto));
				
				if(minutosIniciais<=minutosFinais)
				{
					horarioConvertido = converterHoraMinuto(new Integer(inicioHora), new Integer(inicioMinuto), null, null, new Integer(intervaloHora), new Integer(intervaloMinuto));
					
					aula = preencherOMHorarioAula(inicioHora, inicioMinuto, horarioConvertido, true);
					aula.setTipoIntervalo(true);
					
					aulas.add(aula);
				}
			}
		} 
		catch (Exception e) 
		{
			Logs.addError("adicionarIntervalo", "adicionarIntervalo");
		}
	}
	
	/**
	 * Faz a conversão de hora e retorna no formato string HH:MM
	 * @author João Paulo
	 * @param hora
	 * @param minuto
	 * @param addHora
	 * @param addMinuto
	 * @return
	 */
	public String converterHoraMinuto(Integer hora, Integer minuto, Integer addHora, Integer addMinuto, Integer addIntervaloHora, Integer addIntervaloMinuto)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.set( Calendar.HOUR_OF_DAY, hora);
			gc.set( Calendar.MINUTE, minuto);
			
			if(addHora != null)   		  { gc.add(Calendar.HOUR, addHora); }
			if(addMinuto != null) 		  { gc.add(Calendar.MINUTE, addMinuto); }
			if(addIntervaloHora!= null)   { gc.add(Calendar.HOUR, addIntervaloHora); }
			if(addIntervaloMinuto!= null) { gc.add(Calendar.MINUTE, addIntervaloMinuto); }
			
			return sdf.format(gc.getTime());
		} 
		catch (Exception e) 
		{
			Logs.addError("converterHoraMinuto", "converterHoraMinuto");
			return null;
		}
	}
	
	public Double converteHoraToInteger(String hora)
	{
		try
		{
			return new Double(hora.substring(0, 2));
		} 
		catch (Exception e) 
		{
			Logs.addError("converteHoraToInteger", "converteHoraToInteger");
			return null;
		}
	}
	
	public Double converteMinutoToInteger(String minuto)
	{
		try
		{
			return new Double(minuto.substring(3, 5));
		} 
		catch (Exception e) 
		{
			Logs.addError("converteHoraToInteger", "converteHoraToInteger");
			return null;
		}
	}
	
	public void buscarUnidadeEscolar()
	{
		try
		{
			if(codigoUnidadeEscolar!=null && codigoUnidadeEscolar.length()>0)
			{
				UnidadeEscolar unidadeEscolar = new UnidadeEscolarDAO().buscarUnidadeEscolarSimples(codigoUnidadeEscolar, null);
				if(unidadeEscolar!=null)
				{
					unidadeEscolarSelecionada = unidadeEscolar;
					nomeUnidadeEscolar = unidadeEscolar.getNome();
				}
			}
		} 
		catch (Exception e) 
		{
			Logs.addError("buscarUnidadeEscolar", "buscarUnidadeEscolar");
		}
	}
	
	public HorarioAula preencherOMHorarioAula(String inicioHora, String inicioMinuto, String horarioConvertido, Boolean indicarIntervalo)
	{
		try
		{
			HorarioAula aula = new HorarioAula();
			aula.setHoraInicio(new Double(inicioHora));
			aula.setMinutoInicio(new Double(inicioMinuto));
			
			if(indicarIntervalo) { aula.setInicioAux(inicioHora + ":" + inicioMinuto + "(Intervalo)"); }
			else 				 { aula.setInicioAux(inicioHora + ":" + inicioMinuto); }
			
			aula.setHoraTermino(converteHoraToInteger(horarioConvertido));
			aula.setMinutoTermino(converteMinutoToInteger(horarioConvertido));
			
			if(indicarIntervalo) { aula.setTerminoAux(horarioConvertido + "(Intervalo)"); }
			else 				 { aula.setTerminoAux(horarioConvertido); }
			
			return aula;
		} 
		catch (Exception e) 
		{
			Logs.addError("preencherOMHorarioAula", "preencherOMHorarioAula");
			return null;
		}
	}
	
	/**
	 * Remove todas as aulas da tabela
	 */
	public void removerTodasAulas()
	{
		try 
		{
			aulas = new ArrayList<HorarioAula>();
		} 
		catch (Exception e) 
		{
			Logs.addError("removerTodasAulas", "removerTodasAulas");
		}
	}
	
	/**
	 * Remove apenas a aulas selecionada na tabela
	 */
	public void removerPermissao()
	{
		List<HorarioAula> listAulasAux = new ArrayList<HorarioAula>();
		HorarioAula aulaSelecionada = null;
		
		if(dataTable!=null && dataTable.getRowData()!=null) { aulaSelecionada = (HorarioAula) dataTable.getRowData(); }
		
		if(aulaSelecionada != null && aulaSelecionada.getPkHorarioAula() != null)
		{
			for (HorarioAula aula : aulas) 
			{
				if(!aula.getPkHorarioAula().equals(aulaSelecionada.getPkHorarioAula()))
				{
					listAulasAux.add(aula);
				}
			}
			
			aulas = listAulasAux;
		}
	}
	
	public void pesquisarHorarios() throws SQLException
	{
		if(unidadeEscolarSelecionada!=null && unidadeEscolarSelecionada.getPkUnidadeEscolar()>0 && turnoDado!=null && turnoDado.getPkTurno()>0)
		{
			Integer horaInicio = 0;
			Integer minutoInicio = 0;
			Integer horaTermino = 0;
			Integer minutoTermino = 0;
			Integer horaIntervalo = 0;
			Integer minutoIntervalo = 0;
			Integer horaHoraAula = 0;
			Integer minutoHoraAula = 0;
			Integer posicaoPonto = -1;
			String inicio = "";
			String termino = "";
			String intervalo = "";
			String horaAula = "";
			
			horario = new HorarioDAO().obtemHorariosPorTurno(unidadeEscolarSelecionada, turnoDado);
			
			//Inicio
			posicaoPonto = horario.getHoraInicio().toString().indexOf(".");
			horaInicio = new Integer(horario.getHoraInicio().toString().substring(0, posicaoPonto));
			posicaoPonto = horario.getMinutoInicio().toString().indexOf(".");
			minutoInicio = new Integer(horario.getMinutoInicio().toString().substring(0, posicaoPonto));
			
			if(horaInicio.toString().length() == 1) 	{ inicio += "0" + horaInicio; }
			else 										{ inicio += horaInicio; }
			
			if(minutoInicio.toString().length() == 1) 	{ inicio += ":0" + minutoInicio; }
			else 										{ inicio += ":" + minutoInicio; }
			
			//Termino
			posicaoPonto = horario.getHoraTermino().toString().indexOf(".");
			horaTermino = new Integer(horario.getHoraTermino().toString().substring(0, posicaoPonto));
			posicaoPonto = horario.getMinutoTermino().toString().indexOf(".");
			minutoTermino = new Integer(horario.getMinutoTermino().toString().substring(0, posicaoPonto));
			
			if(horaTermino.toString().length() == 1) 	{ termino += "0" + horaTermino; }
			else 										{ termino += horaTermino; }
			
			if(minutoTermino.toString().length() == 1) 	{ termino += ":0" + minutoTermino; }
			else 										{ termino += ":" + minutoTermino; }
			
			//Intervalo
			posicaoPonto = horario.getHoraIntervalo().toString().indexOf(".");
			horaIntervalo = new Integer(horario.getHoraIntervalo().toString().substring(0, posicaoPonto));
			posicaoPonto = horario.getMinutoIntervalo().toString().indexOf(".");
			minutoIntervalo = new Integer(horario.getMinutoIntervalo().toString().substring(0, posicaoPonto));
			
			if(horaIntervalo.toString().length() == 1) 	{ intervalo += "0" + horaIntervalo; }
			else 										{ intervalo += horaIntervalo; }
			
			if(minutoIntervalo.toString().length() == 1) 	{ intervalo += ":0" + minutoIntervalo; }
			else 											{ intervalo += ":" + minutoIntervalo; }
			
			//Hora Aula
			posicaoPonto = horario.getHoraHoraAula().toString().indexOf(".");
			horaHoraAula = new Integer(horario.getHoraHoraAula().toString().substring(0, posicaoPonto));
			posicaoPonto = horario.getMinutoHoraAula().toString().indexOf(".");
			minutoHoraAula = new Integer(horario.getMinutoHoraAula().toString().substring(0, posicaoPonto));
			
			if(horaHoraAula.toString().length() == 1) 	{ horaAula+= "0" + horaHoraAula; }
			else 										{ horaAula += horaHoraAula; }
			
			if(minutoHoraAula.toString().length() == 1) 	{ horaAula += ":0" + minutoHoraAula; }
			else 											{ horaAula += ":" + minutoHoraAula; }
			
			horario.setInicioAux(inicio);
			horario.setTerminoAux(termino);
			horario.setIntervaloAux(intervalo);
			horario.setHoraAulaAux(horaAula);
			
			aulas = horario.getHorariosAula();
			for (HorarioAula horarioAula : aulas) 
			{
				inicio = "";
				termino = "";
				
				//Inicio
				posicaoPonto = horarioAula.getHoraInicio().toString().indexOf(".");
				horaInicio = new Integer(horarioAula.getHoraInicio().toString().substring(0, posicaoPonto));
				posicaoPonto = horarioAula.getMinutoInicio().toString().indexOf(".");
				minutoInicio = new Integer(horarioAula.getMinutoInicio().toString().substring(0, posicaoPonto));
				
				if(horaInicio.toString().length() == 1) 	{ inicio += "0" + horaInicio; }
				else 										{ inicio += horaInicio; }
				
				if(minutoInicio.toString().length() == 1) 	{ inicio += ":0" + minutoInicio; }
				else 										{ inicio += ":" + minutoInicio; }
				
				//Termino
				posicaoPonto = horarioAula.getHoraTermino().toString().indexOf(".");
				horaTermino = new Integer(horarioAula.getHoraTermino().toString().substring(0, posicaoPonto));
				posicaoPonto = horarioAula.getMinutoTermino().toString().indexOf(".");
				minutoTermino = new Integer(horarioAula.getMinutoTermino().toString().substring(0, posicaoPonto));
				
				if(horaTermino.toString().length() == 1) 	{ termino += "0" + horaTermino; }
				else 										{ termino += horaTermino; }
				
				if(minutoTermino.toString().length() == 1) 	{ termino += ":0" + minutoTermino; }
				else 										{ termino += ":" + minutoTermino; }
				
				if(horarioAula.getTipoIntervalo())
				{
					inicio += "(Intervalo)";
					termino += "(Intervalo)";
				}
				
				horarioAula.setInicioAux(inicio);
				horarioAula.setTerminoAux(termino);
			}
		}
	}
	
	/*
	 * Metodo para limpar o formulario apos cadastro realizado
	 * 
	 * */
	public void limparFormulario() throws SQLException{
		
		turnoDado = new Turno();
		unidadeEscolarSelecionada = new UnidadeEscolar();
		codigoUnidadeEscolar = "";
		nomeUnidadeEscolar = "";
		horario = new Horario();
		aulas = new ArrayList<HorarioAula>();
	}

	private HtmlDataTable dataTable;
	private HtmlDataTable dataTableAulas;
	   
    public HtmlDataTable getDataTable() {
          return dataTable;
    }

    public void setDataTable(HtmlDataTable dataTable) {
          this.dataTable = dataTable;
    }            

    public HtmlDataTable getDataTableAulas() {
		return dataTableAulas;
	}

	public void setDataTableAulas(HtmlDataTable dataTableAulas) {
		this.dataTableAulas = dataTableAulas;
	}
	
	/* GETTERS AND SETTERS */
	public String getCodigoUnidadeEscolar() {
		return codigoUnidadeEscolar;
	}

	public void setCodigoUnidadeEscolar(String codigoUnidadeEscolar) {
		this.codigoUnidadeEscolar = codigoUnidadeEscolar;
	}

	public String getNomeUnidadeEscolar() {
		return nomeUnidadeEscolar;
	}

	public void setNomeUnidadeEscolar(String nomeUnidadeEscolar) {
		this.nomeUnidadeEscolar = nomeUnidadeEscolar;
	}

	public Turno getTurnoDado() {
		return turnoDado;
	}

	public void setTurnoDado(Turno turnoDado) {
		this.turnoDado = turnoDado;
	}

	public Horario getHorario() {
		return horario;
	}

	public void setHorario(Horario horario) {
		this.horario = horario;
	}

	public Boolean getBtRemoverEnabled() {
		return btRemoverEnabled;
	}

	public void setBtRemoverEnabled(Boolean btRemoverEnabled) {
		this.btRemoverEnabled = btRemoverEnabled;
	}

	public List<HorarioAula> getAulas() {
		return aulas;
	}

	public void setAulas(List<HorarioAula> aulas) {
		this.aulas = aulas;
	}

	public UnidadeEscolar getUnidadeEscolarSelecionada() {
		return unidadeEscolarSelecionada;
	}

	public void setUnidadeEscolarSelecionada(UnidadeEscolar unidadeEscolarSelecionada) {
		this.unidadeEscolarSelecionada = unidadeEscolarSelecionada;
	}
}
