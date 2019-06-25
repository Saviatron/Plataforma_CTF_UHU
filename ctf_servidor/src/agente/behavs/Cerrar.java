package agente.behavs;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;

public class Cerrar extends OneShotBehaviour {

	private static final long serialVersionUID = -5765229023142770299L;

	@Override
	public void action() {

		// double empiezo=System.currentTimeMillis();
		// double limit=10000;

		// try {
		// AMSAgentDescription[] agents = null;
		// SearchConstraints c = new SearchConstraints();
		// c.setMaxResults(new Long(-1));
		// agents = AMSService.search(myAgent, new AMSAgentDescription(), c);
		//
		// for (int i = 0; i < agents.length; i++) {
		// AID agentID = agents[i].getName();
		// // if (!agentID.equals(myAgent.getAID()) &&
		// // !agentID.equals(myAgent.getAMS())
		// // && !agentID.equals(myAgent.getDefaultDF())) {
		// System.out.println("Voy a eliminar el agente: " +
		// agentID.getLocalName());
		// KillAgent ka = new KillAgent();
		// ka.setAgent(agentID);
		// Action a = new Action();
		// a.setActor(myAgent.getAMS());
		// a.setAction(ka);
		//
		// ACLMessage AMSRequest = new ACLMessage(ACLMessage.REQUEST);
		// Codec codec = new SLCodec();
		// myAgent.getContentManager().registerLanguage(codec);
		// myAgent.getContentManager().registerOntology(JADEManagementOntology.getInstance());
		// AMSRequest.addReceiver(myAgent.getAMS());
		// AMSRequest.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
		// AMSRequest.setOntology(JADEManagementOntology.getInstance().getName());
		// myAgent.getContentManager().fillContent(AMSRequest, a);
		// myAgent.send(AMSRequest);
		//
		// // } else {
		// // System.out.println("NO voy a eliminar el agente: " +
		// // agentID.getLocalName());
		// // }
		// }
		// } catch (Exception fe) {
		// fe.printStackTrace();
		// }
		//
		System.out.println("Empiezo a cerrar...");
		ACLMessage shutdownMessage = new ACLMessage(ACLMessage.REQUEST);
		Codec codec = new SLCodec();
		myAgent.getContentManager().registerLanguage(codec);
		myAgent.getContentManager().registerOntology(JADEManagementOntology.getInstance());
		shutdownMessage.addReceiver(myAgent.getAMS());
		shutdownMessage.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
		shutdownMessage.setOntology(JADEManagementOntology.getInstance().getName());
		try {
			myAgent.getContentManager().fillContent(shutdownMessage,
					new Action(myAgent.getAID(), new ShutdownPlatform()));
			myAgent.send(shutdownMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Cierro...");

		try {
			AMSAgentDescription[] agents = null;
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults(new Long(-1));
			agents = AMSService.search(myAgent, new AMSAgentDescription(), c);

			for (int i = 0; i < agents.length; i++) {
				AID agentID = agents[i].getName();
				if (agentID.equals(myAgent.getAID())) {
					System.out.println("Voy a eliminar el agente: " + agentID.getLocalName());
					KillAgent ka = new KillAgent();
					ka.setAgent(agentID);
					Action a = new Action();
					a.setActor(myAgent.getAMS());
					a.setAction(ka);

					ACLMessage AMSRequest = new ACLMessage(ACLMessage.REQUEST);
					codec = new SLCodec();
					myAgent.getContentManager().registerLanguage(codec);
					myAgent.getContentManager().registerOntology(JADEManagementOntology.getInstance());
					AMSRequest.addReceiver(myAgent.getAMS());
					AMSRequest.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
					AMSRequest.setOntology(JADEManagementOntology.getInstance().getName());
					myAgent.getContentManager().fillContent(AMSRequest, a);
					myAgent.send(AMSRequest);

				} else {
					System.out.println("NO voy a eliminar el agente: " + agentID.getLocalName());
				}
			}
		} catch (Exception fe) {
			fe.printStackTrace();
		}
	}
}
