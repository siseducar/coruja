package modulos.sisEducar.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modulos.RH.om.Aluno;
import modulos.RH.om.Endereco;
import modulos.RH.om.Pessoa;
import modulos.RH.om.UnidadeEscolar;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class PoiLeitorArquivoExcel 
{
	public static void main(String[] args) 
	{
		try 
		{
			FileInputStream fileInputStream = new FileInputStream("D:\\CADASTRO ALUNO.xls");
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet worksheet = workbook.getSheet("Plan1");
			HSSFRow linha = null;
			HSSFCell coluna = null;
			Map<Integer, List<Object>> map = new HashMap<Integer, List<Object>>();
			List<Object> listAux = new ArrayList<Object>();
			
			for (int i = 1; i < worksheet.getLastRowNum(); i++) 
			{
				listAux = new ArrayList<Object>();
				
				//Obtem a linha
				linha = worksheet.getRow(i);
				
				coluna = linha.getCell((int) 0); //COD
				listAux.add(coluna);
				
				coluna = linha.getCell((int) 1); //UNIDADE
				listAux.add(coluna);
				
				coluna = linha.getCell((short) 2); //NOME
				listAux.add(coluna);
				
				coluna = linha.getCell((short) 3); //SEXO
				listAux.add(coluna);
				
				coluna = linha.getCell((short) 4); //DTNASCIMENTO
				listAux.add(coluna);
				
				coluna = linha.getCell((short) 5); //RA1
				listAux.add(coluna);
				
				coluna = linha.getCell((short) 6); //RA2
				listAux.add(coluna);
				
				coluna = linha.getCell((short) 7); //UF
				listAux.add(coluna);
				
				coluna = linha.getCell((short) 8); //NOME PAI
				listAux.add(coluna);
				
				coluna = linha.getCell((short) 9); //NOME M�E
				listAux.add(coluna);
				
				coluna = linha.getCell((short) 10); //NOME RUA
				listAux.add(coluna);
				
				coluna = linha.getCell((short) 11); //N�MERO
				listAux.add(coluna);
				
				coluna = linha.getCell((short) 12); //BAIRRO
				listAux.add(coluna);
				
				coluna = linha.getCell((short) 13); //CEP
				listAux.add(coluna);
				
				coluna = linha.getCell((short) 14); //CIDADE
				listAux.add(coluna);
				
				coluna = linha.getCell((short) 15); //FOLHA
				listAux.add(coluna);
				
				coluna = linha.getCell((short) 16); //LIVRO
				listAux.add(coluna);
				
				coluna = linha.getCell((short) 17); //REGISTRO
				listAux.add(coluna);
				
				coluna = linha.getCell((short) 18); //CIDADENA SC
				listAux.add(coluna);
				
				coluna = linha.getCell((short) 19); //LIVRO UF
				listAux.add(coluna);
				
				map.put(i, listAux);
			}
			
			Integer countPosicao = 0;
			List<Object> listaAuxiliar = null;
			Aluno aluno = null;
			UnidadeEscolar unidadeEscolar = null;
			Pessoa pessoa = null;
			Endereco endereco = null;
			
			for (Map.Entry<Integer, List<Object>> entry : map.entrySet()) 
			{
				aluno = new Aluno();
				unidadeEscolar = new UnidadeEscolar();
				pessoa = new Pessoa();
				endereco = new Endereco();
				listaAuxiliar = new ArrayList<Object>();
				listaAuxiliar = entry.getValue();
				HSSFCell cell = null;
				
				//Monta a unidade
				cell = (HSSFCell) listaAuxiliar.get(1);
				
				unidadeEscolar.setCodigo(cortarCasasDecimais(new Double(cell.getNumericCellValue()).toString(), false));
				unidadeEscolar.setNome("nd");
				unidadeEscolar.setStatus(ConstantesSisEducar.STATUS_ATIVO);
				
				//Monta o endere�o
				cell = (HSSFCell) listaAuxiliar.get(10);
				endereco.setLogradouro(cell.getStringCellValue());

				cell = (HSSFCell) listaAuxiliar.get(11);
				endereco.setNumero(new Integer(cortarCasasDecimais(new Double(cell.getNumericCellValue()).toString(), false)));
				
				cell = (HSSFCell) listaAuxiliar.get(12);
				endereco.setBairro(cell.getStringCellValue());
				
				cell = (HSSFCell) listaAuxiliar.get(13);
				String cep = cortarCasasDecimais(new Double(cell.getNumericCellValue()).toString(), true);
				
				endereco.setCep(new Integer(cep));
				
				//Monta o aluno
				aluno.setRa((String)listaAuxiliar.get(5)); //RA
				aluno.setRa2((String)listaAuxiliar.get(6)); //RA2
				aluno.setNomePai((String)listaAuxiliar.get(8)); //Nome Pai
				aluno.setNomeMae((String)listaAuxiliar.get(9)); //Nome M�e
				aluno.setFolha((String)listaAuxiliar.get(15));
				aluno.setLivro((String)listaAuxiliar.get(16));
				aluno.setRegistro((Integer)listaAuxiliar.get(17));
				
				//Cidade
				aluno.setLivroUF((String)listaAuxiliar.get(19));
				
				//Monta a pessoa
				pessoa.setNome((String)listaAuxiliar.get(2)); //Nome
				pessoa.setSexo((String)listaAuxiliar.get(3)); //Sexo
				
				//UF - Criar OM Estado
				//Cidade - Criar OM Cidade
				endereco.setLogradouro((String)listaAuxiliar.get(10));
				endereco.setNumero((Integer)listaAuxiliar.get(11));
				endereco.setBairro((String)listaAuxiliar.get(12));
				endereco.setCep((Integer)listaAuxiliar.get(13));
				
				pessoa.setEndereco(endereco);
				
				listaAuxiliar.get(1);
			    pessoa.setUnidadeEscolar(unidadeEscolar);
			    
			    countPosicao ++;
			}
			
			System.out.println(map.get(1));
		} 
		catch (FileNotFoundException e) { e.printStackTrace(); }
		catch (IOException e) 			{ e.printStackTrace(); }
	}
	
	public static String cortarCasasDecimais(String numeroOrigem, Boolean validarCEP)
	{
		Integer posicaoPonto = 0;
		String numeroDestino = "";
		
		if(numeroOrigem!=null && numeroOrigem.length() >0)
		{
			if(validarCEP)
			{
				Integer numeroDe0 = 0;
				Integer posicaoCortar = 0;
				String stringNova = "";
				numeroDestino = numeroOrigem.replace(".", "");
				numeroDe0 = numeroDestino.indexOf("E");
				posicaoCortar = numeroDestino.indexOf("E");
				stringNova = numeroDestino.substring(0, posicaoCortar);
				for (int i = 0; i < numeroDe0; i++) 
				{
					stringNova += "0";
				}
				return stringNova;
			}
			
			posicaoPonto = numeroOrigem.indexOf(".");
			numeroDestino = numeroOrigem.substring(0, posicaoPonto);
			return numeroDestino;
		}
		
		return null;
	}
}