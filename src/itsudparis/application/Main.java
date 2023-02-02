/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package itsudparis.application;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.util.*;

import java.lang.Thread;

import com.hp.hpl.jena.rdf.model.Model;
import itsudparis.tools.JenaEngine;

/**
 *
 * @author DO.ITSUDPARIS
 */
public class Main {
  

	public static String insertNameInQuery(String query, int queryChoice){
		System.out.println(query);
		query = query.trim();
		if(queryChoice == 1){
		return
				"PREFIX ns: <http://www.semanticweb.org/louis/ontologies/2023/0/untitled-ontology-10#>" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
				"\n" +
				"\n" +
				"SELECT ?a ?num_a ?mass_a ?radioactif ?elec\n" +
				"WHERE {\n" +
						"?a ns:nom \"" + query+ "\".\n" +
						"?a ns:numéro_atomique ?num_a .\n" +
						"?a ns:masse_atomique ?mass_a .\n" +
						"?a ns:masse_atomique ?mass_a .\n" +
						"?a ns:radioactif ?radioactif .\n" +
						"OPTIONAL{?a ns:électronégativité ?elec .}\n" +

						"}";
		}
		else if (queryChoice == 3) {
			return  "PREFIX ns: <http://www.semanticweb.org/louis/ontologies/2023/0/untitled-ontology-10#>\n" +
					"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
					"\n" +
					"\n" +
					"SELECT ?atomes ?symbole ?nom ?masse_atomique ?numéro_atomique\n"+
					"WHERE {\n"+
    						"?atomes rdf:type ns:Atome.\n"+
							"?atomes ns:symbole ?symbole.\n" +
							"OPTIONAL {?atomes ns:masse_atomique ?masse_atomique}\n" +
     						"?atomes ns:numéro_atomique ?numéro_atomique. FILTER(?numéro_atomique >=10)\n"+
							"OPTIONAL {?atomes ns:nom ?nom}\n" +
							"}";
		}

		else if (queryChoice==2){
			return
					"PREFIX ns: <http://www.semanticweb.org/louis/ontologies/2023/0/untitled-ontology-10#>\n" +
							"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
							"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
			"SELECT ?atomes ?symbole ?nom ?masse_atomique ?numéro_atomique\n" +
					"WHERE { \n" +
						"?atomes rdf:type ns:Atome.\n" +
						"?atomes ns:symbole ?symbole.\n" +
						"OPTIONAL {?atomes ns:masse_atomique ?masse_atomique}\n" +
						"OPTIONAL {?atomes ns:numéro_atomique ?numéro_atomique}\n" +
						"OPTIONAL {?atomes ns:nom ?nom }\n" +
					"}";
		} else if(queryChoice == 4){
			return
					"PREFIX ns: <http://www.semanticweb.org/louis/ontologies/2023/0/untitled-ontology-10#>\n" +
							"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
							"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
							"\n" +
							"\n" +
							"SELECT ?a ?num_a\n" +
							"WHERE {\n" +
							"?mol ns:nom \"" + query + "\"\n"+
							"?a rdf:type ns:Atome.\n" +
							"?a ns:est_present_dans ?mol .\n" +
							"?a ns:numéro_atomique ?num_a .\n" +
							"}";
		} else if(queryChoice == 5){
			return
					"PREFIX ns: <http://www.semanticweb.org/louis/ontologies/2023/0/untitled-ontology-10#>\n" +
							"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
							"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
							"\n" +
							"\n" +
							"SELECT ?name \n" +
							"WHERE {\n" +
							"?a rdf:type ns:Atome.\n" +
							"?a ns:nom ?name.\n" +
							"FILTER(regex(?name, \"H.*\" ))\n" +
							"}";
		} else if(queryChoice == 6){
			return
					"PREFIX ns: <http://www.semanticweb.org/louis/ontologies/2023/0/untitled-ontology-10#>\n" +
							"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
							"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
							"\n" +
							"\n" +
							"SELECT ?a ?m ?symb_mol ?symb_atome \n" +
							"WHERE {\n" +
							"?a ns:est_présent_dans ?m.\n"+
							"?m ns:symbole ?symb_mol.\n"+
							"?a ns:symbole ?symb_atome.\n"+
							"}";
		}

		else if(queryChoice == 7){
			return
					"PREFIX ns: <http://www.semanticweb.org/louis/ontologies/2023/0/untitled-ontology-10#>\n" +
							"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
							"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
							"\n" +
							"\n" +
							"SELECT ?at ?famille \n" +
							"WHERE {\n" +
							"?at ns:appartient_a_la_famille_element ?famille.\n" +
							"}";
		}
		return "";


	}

	public static void writeRulesForTable(int [] famille, String famille_name) throws IOException {
		String filePath = new File("").getAbsolutePath();
		String data_location = "/data/rules.txt";
		filePath = filePath + data_location;

		FileWriter writer = new FileWriter(filePath, true);
		BufferedWriter bw = new BufferedWriter(writer);
		PrintWriter out = new PrintWriter(bw);

		boolean writeLine = true;
		for(int i=0; i< famille.length; i++){
			File file = new File(filePath);

			String s = "[rule" + famille[i] + ": (?atome rdf:type ns:Atome) (?atome ns:numéro_atomique ?num)" +
					" (?famille ns:nom \""+famille_name+"\") equal(" + famille[i] + " ,?num) ->" +
					" (?atome ns:appartient_a_la_famille_element ?famille)]";
			writeLine = true;
			try {
				Scanner scanner = new Scanner(file);
				//now read the file line by line...
				int lineNum = 0;
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					lineNum++;
					if(line.equals(s)) {
						writeLine = false;
					}
				}
			} catch(FileNotFoundException e) {
			}
			if(writeLine){
				out.println(s);
			}
		}
		out.close();
		bw.close();
		writer.close();
	}

public static void main(String[] args) throws IOException, InterruptedException {
	//on indique le chemin vers jena-log4j.properties (pour supprimer les warnings)

	int [] non_metaux = {1,6,7,8,15,16,34};
	writeRulesForTable(non_metaux, "non_métaux");

	int [] gaz_rares = {2,10,18};
	writeRulesForTable(gaz_rares, "gaz_rares");

	String NS = "";
	// lire le model a partir d'une ontologie
	Model model = JenaEngine.readModel("data/atom_ontology_java_rdf.owl");
	InfModel infModel = ModelFactory.createRDFSModel(model);
		while (true) {
			if (model != null) {
				//lire le Namespace de l’ontologie
				NS = model.getNsPrefixURI("");


				Scanner sc = new Scanner(System.in);    //System.in is a standard input stream
				System.out.println("Select a request number:\n 1 - Select one atom in particular \n 2 - Select all atoms \n 3 - Select all atoms whose atomic number is > 10\n 4 - Select atoms present in chosen molecule \n 5 - Atoms that start with H \n 6 - Select molecules and atoms in these molecules \n 7 - Select chemical families   ");


				System.out.print("Enter choice of query: ");
				int choice = sc.nextInt();

				Scanner sc_name = new Scanner(System.in);
				String nameToSearch;
				if(choice == 1 || choice == 4){
				System.out.print("Enter name: ");
				nameToSearch = sc_name.nextLine();
				} else {
				nameToSearch="";
				}

				// adding capital letter if user forgot to
				if(!(nameToSearch.isEmpty() || nameToSearch.isEmpty())){
					if(Character.isLowerCase(nameToSearch.charAt(0))){
						nameToSearch = nameToSearch.replace(nameToSearch.charAt(0),Character.toUpperCase(nameToSearch.charAt(0)));
					}
				}
				String query = insertNameInQuery(nameToSearch,choice);

				//apply owl rules on the model
//				Model owlInferencedModel =
//					JenaEngine.readInferencedModelFromRuleFile(model, "data/owlrules.txt");

				// apply our rules on the owlInferencedModel
				Model inferedModel =
					JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");

				// query on the model after inference
				System.out.println(JenaEngine.executeQuery(inferedModel, query));
		} else{
			System.out.println("Error when reading model from ontology");
		}
			Thread.sleep(1000);
	}
	 
}
}

