/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package itsudparis.application;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.*;
import java.io.FileWriter;   // Import the FileWriter class

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
		else if (queryChoice == 2) {
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

		else if (queryChoice==3){
			return
					"PREFIX ns: <http://www.semanticweb.org/louis/ontologies/2023/0/untitled-ontology-10#>\n" +
							"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
							"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
			"SELECT ?atomes ?symbole ?nom ?masse_atomique ?numéro_atomique\n" +
					"WHERE { \n" +
					"     ?atomes rdf:type ns:Atome.\n" +
					"     ?atomes ns:symbole ?symbole.\n" +
					"     OPTIONAL {?atomes ns:masse_atomique ?masse_atomique}\n" +
					"    OPTIONAL {?atomes ns:numéro_atomique ?numéro_atomique}\n" +
					"    OPTIONAL {?atomes ns:nom ?nom }\n" +
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
							"?a rdf:type ns:Molécule.\n" +
							"?a ns:nom ?name.\n" +
							"FILTER(regex(?name, \"H.*\" ))\n" +
							"}";
		}
		return "";
	}

public static void main(String[] args) throws IOException, InterruptedException {
	//on indique le chemin vers jena-log4j.properties (pour supprimer les warnings)
//	String log4jConfPath = "/Users/amelbouzeghoub/Documents/Enseignement/ASR2020-2021/jena10/apache-jena-2.10.0/jena-log4j.properties";
//	PropertyConfigurator.configure(log4jConfPath);
	//fin de la config log4j
	String NS = "";
	// lire le model a partir d'une ontologie
	Model model = JenaEngine.readModel("data/atom_ontology_java_rdf.owl");
	InfModel infModel = ModelFactory.createRDFSModel(model);
	infModel.remove(null, null, null);
		while (true) {
			if (model != null) {
				//lire le Namespace de l’ontologie
				NS = model.getNsPrefixURI("");

				// modifier le model
				// Ajouter une nouvelle femme dans le modele: Nora,50, estFilleDe Peter

				Scanner sc = new Scanner(System.in);    //System.in is a standard input stream
				System.out.print("Enter name: ");
				String nameToSearch = sc.nextLine();

				System.out.print("Enter choice of query: ");
				int choice = sc.nextInt();

				FileWriter myWriter = new FileWriter("data/query.txt");
				// adding capital letter if user forgot to
				if(!(nameToSearch.isEmpty() || nameToSearch.isEmpty())){
					if(Character.isLowerCase(nameToSearch.charAt(0))){
						nameToSearch = nameToSearch.replace(nameToSearch.charAt(0),Character.toUpperCase(nameToSearch.charAt(0)));
					}
				}
				String query = insertNameInQuery(nameToSearch,choice);
				myWriter.write(query);

				//apply owl rules on the model
				Model owlInferencedModel =
					JenaEngine.readInferencedModelFromRuleFile(model, "data/owlrules.txt");

				// apply our rules on the owlInferencedModel
				Model inferedModel =
					JenaEngine.readInferencedModelFromRuleFile(owlInferencedModel, "data/rules.txt");


				// query on the model after inference
				System.out.println(JenaEngine.executeQuery(inferedModel, query));
		} else{
			System.out.println("Error when reading model from ontology");
		}
			Thread.sleep(1000);
	}
	 
}
}

