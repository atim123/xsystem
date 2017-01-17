/*
 * Copyright 2017 Andrey Timofeev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xsystem.bpmn2.formats.xml;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConvertingWrapDynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSInput;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xsystem.bpmn2.formats.BPMN2Factory;
import org.xsystem.bpmn2.formats.BPMNDIFactory;
import org.xsystem.bpmn2.formats.Parser;
import org.xsystem.bpmn2.model.activities.Activity;
import org.xsystem.bpmn2.model.activities.MultiInstanceBehavior;
import org.xsystem.bpmn2.model.activities.MultiInstanceLoopCharacteristics;
import org.xsystem.bpmn2.model.activities.ScriptTask;
import org.xsystem.bpmn2.model.bpmndi.*;
import org.xsystem.bpmn2.model.collaboration.Collaboration;
import org.xsystem.bpmn2.model.collaboration.MessageFlow;
import org.xsystem.bpmn2.model.collaboration.Participant;
import org.xsystem.bpmn2.model.common.*;
import org.xsystem.bpmn2.model.conversation.Conversation;
import org.xsystem.bpmn2.model.data.DataAssociation;
import org.xsystem.bpmn2.model.data.DataInputAssociation;
import org.xsystem.bpmn2.model.data.DataOutputAssociation;
import org.xsystem.bpmn2.model.foundation.BaseElement;
import org.xsystem.bpmn2.model.foundation.RootElement;
import org.xsystem.bpmn2.model.gateways.GatewayDirection;
import org.xsystem.bpmn2.model.infrastructure.Definitions;
import org.xsystem.bpmn2.model.process.Lane;
import org.xsystem.bpmn2.model.process.LaneSet;
import org.xsystem.bpmn2.model.process.Process;
import org.xsystem.bpmn2.model.process.ProcessType;
import org.xsystem.bpmn2.model.service.Interface;
import org.xsystem.bpmn2.model.service.Operation;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.activities.MultiInstanceLoopCharacteristicsImpl;
import org.xsystem.bpmn2.modelimpl.common.FormalExpressionImpl;
import org.xsystem.bpmn2.modelimpl.data.DataInputAssociationImpl;
import org.xsystem.bpmn2.modelimpl.data.DataOutputAssociationImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.ImportImpl;
import org.xsystem.bpmn2.modelimpl.process.LaneSetImpl;
import org.xsystem.utils.LSInputImpl;
import org.xsystem.utils.XMLUtil;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.xsystem.bpmn2.model.activities.Rendering;
import org.xsystem.bpmn2.model.activities.ResourceAssignmentExpression;
import org.xsystem.bpmn2.model.activities.ResourceRole;
import org.xsystem.bpmn2.model.activities.UserTask;
import org.xsystem.bpmn2.model.collaboration.ParticipantMultiplicity;
import org.xsystem.bpmn2.model.data.Assignment;
import org.xsystem.bpmn2.model.data.DataInput;
import org.xsystem.bpmn2.model.data.DataOutput;
import org.xsystem.bpmn2.model.data.InputOutputSpecification;
import org.xsystem.bpmn2.model.data.InputSet;
import org.xsystem.bpmn2.model.data.OutputSet;
import org.xsystem.bpmn2.model.events.CatchEvent;
import org.xsystem.bpmn2.model.events.EventDefinition;
import org.xsystem.bpmn2.model.events.TerminateEventDefinition;
import org.xsystem.bpmn2.model.events.ThrowEvent;
import org.xsystem.bpmn2.model.events.TimerEventDefinition;
import org.xsystem.bpmn2.modelimpl.activities.RenderingImpl;
import org.xsystem.bpmn2.modelimpl.activities.ResourceAssignmentExpressionImpl;
import org.xsystem.bpmn2.modelimpl.data.AssignmentImpl;
import org.xsystem.bpmn2.modelimpl.data.DataInputImpl;
import org.xsystem.bpmn2.modelimpl.data.DataOutputImpl;
import org.xsystem.bpmn2.modelimpl.data.InputOutputSpecificationImpl;
import org.xsystem.bpmn2.modelimpl.data.InputSetImpl;
import org.xsystem.bpmn2.modelimpl.data.OutputSetImpl;
import org.xsystem.bpmn2.modelimpl.events.MessageEventDefinitionImpl;
import org.xsystem.bpmn2.modelimpl.events.TerminateEventDefinitionImpl;
import org.xsystem.bpmn2.modelimpl.events.TimerEventDefinitionImpl;
import org.xsystem.bpmn2.modelimpl.humaninteraction.HumanPerformerImpl;
import org.xsystem.bpmn2.modelimpl.humaninteraction.PotentialOwnerImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class XMLParser3 implements Parser{
    public static final String BPMN2_NS = "http://www.omg.org/spec/BPMN/20100524/MODEL";
    public static final String BPMNDI_NS = "http://www.omg.org/spec/BPMN/20100524/DI";
    public static final String DC_NS = "http://www.omg.org/spec/DD/20100524/DC";
    public static final String DI_NS = "http://www.omg.org/spec/DD/20100524/DI";
    public static final String XSYSTEMBPMN = "http://xsystem.org/bpmn";

    DefinitionsImpl definitions;
    Process currentProcess;

    Schema buildSxema() throws SAXException, IOException {
        final ClassLoader classLoader = this.getClass().getClassLoader();
                //Thread.currentThread().getContextClassLoader();

        try (InputStream is = classLoader.getResourceAsStream("BPMN20/BPMN20.xsd")) {

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);//"http://www.w3.org/2001/XMLSchema");
            StreamSource ss = new StreamSource(is);
            factory.setResourceResolver((String type, String namespaceURI, String publicId, String systemId, String baseURI) -> {
                InputStream resourceAsStream = classLoader
                        .getResourceAsStream("BPMN20/" + systemId);
                LSInput ret = new LSInputImpl(publicId, systemId, resourceAsStream);
                return ret;
            });
            Schema shema = factory.newSchema(ss);
            return shema;
        }
    }

    Document validate(InputStream input) throws ParserConfigurationException, SAXException, IOException {
        Schema schema = buildSxema();
        Validator validator = schema.newValidator();

        validator.setErrorHandler(new ErrorHandler() {

            @Override
            public void warning(SAXParseException ex) throws SAXException {
                System.err.println(ex.getMessage());
            }

            @Override
            public void error(SAXParseException ex) throws SAXException {
                System.err.println(ex.getMessage());
            }

            @Override
            public void fatalError(SAXParseException ex) throws SAXException {
                throw ex;
            }

        }
        );

        Document doc = XMLUtil.getDocument(input);

        DOMSource source = new DOMSource(doc);

        validator.validate(source);//, result);

        return doc;
    }

    protected void setNameSpace(Element elem) {
        NamedNodeMap atts = elem.getAttributes();
        for (int i = 0; i < atts.getLength(); i++) {
            Node currentAttribute = atts.item(i);
            String currentPrefix = currentAttribute.getPrefix();
            if (currentPrefix != null && currentPrefix.equals(XMLConstants.XMLNS_ATTRIBUTE)) {
                String prfx = currentAttribute.getLocalName();
                String uri = currentAttribute.getNodeValue();
                definitions.setNamespaceURI(prfx, uri);
            }
        }
    }

    static String getPrefixNs(String ns) {
        int idx = ns.indexOf(":");
        if (idx < 0) {
            return null;
        }
        String ret = ns.substring(0, idx);
        return ret;
    }

    static String getLocal(String ns) {
        int idx = ns.indexOf(":");
        if (idx < 0) {
            return ns;
        }
        String ret = ns.substring(idx + 1);
        return ret;
    }

    void parseAttributes(Object base, Element rootElement) {

        ConvertingWrapDynaBean wrap = new ConvertingWrapDynaBean(base);
        DynaClass danaClass = wrap.getDynaClass();
        NamedNodeMap atts = rootElement.getAttributes();
        String defNamespace = rootElement.getNamespaceURI();
        for (int i = 0; i < atts.getLength(); i++) {
            Node currentAttribute = atts.item(i);
            String ns = currentAttribute.getNamespaceURI();
            if (ns == null) {
                ns = defNamespace;
            }
            String attrName = currentAttribute.getLocalName();
            //  getNodeName();
            switch (ns) {
                case BPMN2_NS: {
                    DynaProperty dynaProperty = danaClass.getDynaProperty(attrName);
                    if (dynaProperty != null) {
                        Class clazz = dynaProperty.getType();
                        if (clazz.isAssignableFrom(Reference.class)) {
                            String value = currentAttribute.getNodeValue();
                            Reference ref = createReference(value);
                            wrap.set(attrName, ref);
                        } else if (clazz.isAssignableFrom(QName.class)) {
                            String value = currentAttribute.getNodeValue();
                            QName ret = createQName(value);
                            wrap.set(attrName, ret);
                        } else if (clazz.isAssignableFrom(MultiInstanceBehavior.class)) {
                            String value = currentAttribute.getNodeValue();
                            wrap.set(attrName, MultiInstanceBehavior.valueOf(value));
                        } else {
                            String value = currentAttribute.getNodeValue();
                            wrap.set(attrName, value);
                        }
                    }
                    break;
                }
                case BPMNDI_NS:
                case DC_NS:
                case DI_NS: {
                    DynaProperty dynaProperty = danaClass.getDynaProperty(attrName);
                    if (dynaProperty != null) {
                        String value = currentAttribute.getNodeValue();
                        wrap.set(attrName, value);
                    }
                    break;
                }
            }
        }
    }

    void parseImport(Element rootElement) {
        ImportImpl inprt = new ImportImpl();
        parseAttributes(inprt, rootElement);
        definitions.getImports().add(inprt);
    }

    void parseDataAssociation(DataAssociation dataAssoc, Element rootElement) {
        parseAttributes(dataAssoc, rootElement);
        List<Element> childs = XMLUtil.elements(rootElement);
        for (Element child : childs) {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            //  getNodeName();

            if (ns.equals(BPMN2_NS)) {
                switch (nodeName) {
                    case "sourceRef": {
                        Reference ref = createReference(child);
                        dataAssoc.getSourceRef().add(ref);
                        break;
                    }
                    case "targetRef": {
                        Reference ref = createReference(child);
                        dataAssoc.setTargetRef(ref);
                        break;
                    }
                    case "assignment":
                        Assignment assignment=pareseAssignment(child);
                        dataAssoc.getAssignment().add(assignment);
                        break;
                }
            }
        }
    }

    Assignment pareseAssignment(Element root){
        Assignment assignment=new AssignmentImpl(definitions);
        parseAttributes(assignment, root);
        List<Element> childs = XMLUtil.elements(root);
        for (Element child : childs) {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            if (ns.equals(BPMN2_NS)) {
                switch (nodeName) {
                   case "from": {
                       Expression from=parseExpression(child);
                       assignment.setFrom(from);
                       break;
                   }
                   case "to": {
                       Expression to=parseExpression(child);
                       assignment.setTo(to);
                       break;
                   }
                }
            }
        }
        return assignment;
    }
    
    void parseMultiInstanceLoopCharacteristics(MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics, Element rootElement) {
        parseAttributes(multiInstanceLoopCharacteristics, rootElement);
        List<Element> childs = XMLUtil.elements(rootElement);
        for (Element child : childs) {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            if (ns.equals(BPMN2_NS)) {
                switch (nodeName) {
                    case "loopCardinality": {
                        Expression expr = parseExpression(child);
                        multiInstanceLoopCharacteristics.setLoopCardinality(expr);
                        break;
                    }
                    default: {
                        System.out.println("(MultiInstanceLoopCharacteristics not imp element " + nodeName);
                    }
                }
            }
        }
    }

    Expression parseExpression(Element child) {

        String exType = child.getAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type");
        if (exType != null && exType.equals("bpmn2:tFormalExpression")) {
            FormalExpression expression = new FormalExpressionImpl(definitions);
            parseAttributes(expression, child);
            String value = XMLUtil.getContentText(child);
            expression.setBody(value);
            return expression;
        } else {
            exType = "mvel";
            FormalExpression expression = new FormalExpressionImpl(definitions);
            expression.setLanguage(exType);
            //  parseAttributes(expression, child);
            String value = XMLUtil.getContentText(child);
            expression.setBody(value);
            return expression;
        }
    }

    void parseInputSet(InputSet inputSet,List<Element> childs){
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            if (ns.equals(BPMN2_NS)) {
                if (nodeName.equals("dataInputRefs")){
                    String value = XMLUtil.getContentText(child);
                    inputSet.getDataInputRefs().add(createReference(value));
                }
            }
        });    
    }
    
    void parseOutputSet(OutputSet outputSet,List<Element> childs){
        
    }
    
    void parseIoSpecification(InputOutputSpecification ioSpecification,List<Element> childs){
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            if (ns.equals(BPMN2_NS)) {
                if (nodeName.equals("dataInput")){
                    DataInput dataInput= new DataInputImpl(definitions);            
                    parseAttributes(dataInput, child);
                    ioSpecification.getDataInputs().add(dataInput);
                } else if (nodeName.equals("inputSet")){
                    InputSet inputSet=new InputSetImpl(definitions);
                    parseAttributes(inputSet, child);
                    ioSpecification.getInputSets().add(inputSet);
                    parseInputSet(inputSet, XMLUtil.elements(child));      
                } if (nodeName.equals("outputSet")){
                    OutputSet outputSet=new OutputSetImpl(definitions);
                    parseAttributes(outputSet, child);
                    ioSpecification.getOutputSets().add(outputSet);
                    parseOutputSet(outputSet,XMLUtil.elements(child));
                }
            }
        });    
    }
    
    /*
    <ioSpecification>
         <dataInput id="_DataInput_30" name="TaskName"/>
         <inputSet>
            <dataInputRefs>_DataInput_30</dataInputRefs>
         </inputSet>
         <outputSet  name="Output Set"/>
      </ioSpecification>
    */
    
    void parseTimerEventDefinitionElement(TimerEventDefinition timerEventDefinition,  List<Element> childs){
         childs.stream().forEach((child) -> {
             String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            if (ns.equals(BPMN2_NS)) {
                if (nodeName.equals("timeDate")){
                    Expression  exp= parseExpression(child);
                    timerEventDefinition.setTimeDate(exp);
                }
            }
         });
    }
    
    //FlowNode outgoing incoming
    void parseFlowElement(FlowElement flowElement, List<Element> childs) {
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            //getNodeName();
            if (ns.equals(BPMN2_NS)) {
                if (nodeName.equals("outgoing") && (flowElement instanceof FlowNode)) {
                    Reference ref = createReference(child);
                    ((FlowNode) flowElement).getOutgoing().add(ref);
                } else if (nodeName.equals("incoming") && (flowElement instanceof FlowNode)) {
                    Reference ref = createReference(child);
                    ((FlowNode) flowElement).getIncoming().add(ref);
                } else if (nodeName.equals("dataInputAssociation") && (flowElement instanceof Activity)) {
                    List<DataInputAssociation> dataAssocList = ((Activity) flowElement).getDatainputAssociations();
                    DataInputAssociation association = new DataInputAssociationImpl(definitions);
                    parseDataAssociation(association, child);
                    dataAssocList.add(association);
                } else if (nodeName.equals("ioSpecification") && (flowElement instanceof Activity)) {
                    Activity activity=(Activity)flowElement;
                    InputOutputSpecification ioSpecification=new InputOutputSpecificationImpl(definitions);
                    activity.setIoSpecification(ioSpecification);
                    parseIoSpecification(ioSpecification, XMLUtil.elements(child));
                 
                } else if (nodeName.equals("dataOutputAssociation") && (flowElement instanceof Activity)) {
                    List<DataOutputAssociation> dataAssocList = ((Activity) flowElement).getDataOutputAssociations();
                    DataOutputAssociation association = new DataOutputAssociationImpl(definitions);
                    parseDataAssociation(association, child);
                    dataAssocList.add(association);
                } else if (nodeName.equals("multiInstanceLoopCharacteristics") && (flowElement instanceof Activity)) {
                    Activity activity = (Activity) flowElement;
                    MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = new MultiInstanceLoopCharacteristicsImpl(definitions);
                    parseMultiInstanceLoopCharacteristics(multiInstanceLoopCharacteristics, child);
                    activity.setLoopCharacteristics(multiInstanceLoopCharacteristics);
                } else if (nodeName.equals("conditionExpression") && (flowElement instanceof SequenceFlow)) {
                    Expression expression = parseExpression(child);
                    ((SequenceFlow) flowElement).setConditionExpression(expression);
                } else if (nodeName.equals("script") && (flowElement instanceof ScriptTask)) {
                    ScriptTask scriptTask = (ScriptTask) flowElement;
                    String value = XMLUtil.getContentText(child);
                    scriptTask.setScript(value);
                } else if (nodeName.equals("rendering") && (flowElement instanceof UserTask)) {
                    UserTask userTask = (UserTask) flowElement;
                    Rendering rendering = new RenderingImpl(definitions);
                    userTask.setRendering(rendering);
                    parseRenderingElement(rendering, XMLUtil.elements(child));
                } else if (nodeName.equals("potentialOwner") && (flowElement instanceof UserTask)) {
                    UserTask userTask = (UserTask) flowElement;
                    ResourceRole potentialOwner = new PotentialOwnerImpl(definitions);
                    userTask.getResources().add(potentialOwner);
                    parseResourceRoleElement(potentialOwner, XMLUtil.elements(child));
                } else if (nodeName.equals("humanPerformer") && (flowElement instanceof UserTask)) {
                    UserTask userTask = (UserTask) flowElement;
                    ResourceRole potentialOwner = new HumanPerformerImpl(definitions);
                    userTask.getResources().add(potentialOwner);
                    parseResourceRoleElement(potentialOwner, XMLUtil.elements(child));
                } else if (nodeName.equals("messageEventDefinition") && (flowElement instanceof CatchEvent)) {
                    CatchEvent catchEvent=(CatchEvent) flowElement;
                    EventDefinition eventDefinition=new MessageEventDefinitionImpl(definitions);
                    parseAttributes(eventDefinition,child);
                    catchEvent.getEventDefinitions().add(eventDefinition);
                } else if (nodeName.equals("timerEventDefinition") && (flowElement instanceof CatchEvent)){
                    CatchEvent catchEvent=(CatchEvent) flowElement;
                    TimerEventDefinition timerEventDefinition=new TimerEventDefinitionImpl(definitions);
                    parseAttributes(timerEventDefinition,child);
                    parseTimerEventDefinitionElement(timerEventDefinition, XMLUtil.elements(child));
                    catchEvent.getEventDefinitions().add(timerEventDefinition);
                } else if (nodeName.equals("terminateEventDefinition") && (flowElement instanceof ThrowEvent)){
                    ThrowEvent throwEvent=(ThrowEvent) flowElement;
                    TerminateEventDefinition terminateEventDefinition=new TerminateEventDefinitionImpl(definitions);
                    parseAttributes(terminateEventDefinition,child);
                    throwEvent.getEventDefinitions().add(terminateEventDefinition);
                } else if ( nodeName.equals("dataOutput")&& (flowElement instanceof CatchEvent)) {
                    CatchEvent catchEvent=(CatchEvent) flowElement;
                    DataOutput dataOutput=new DataOutputImpl(definitions);
                    parseAttributes(dataOutput,child);
                    catchEvent.getDataOutputs().add(dataOutput);
                } else if (nodeName.equals("dataOutputAssociation") && (flowElement instanceof CatchEvent)) {
                    List<DataOutputAssociation> dataAssocList = ((CatchEvent) flowElement).getDataOutputAssociations();
                    DataOutputAssociation association = new DataOutputAssociationImpl(definitions);
                    parseDataAssociation(association, child);
                    dataAssocList.add(association);
                } if ( nodeName.equals("dataInput") && (flowElement instanceof ThrowEvent)){
                    ThrowEvent throwEvent=(ThrowEvent) flowElement;
                    DataInput dataInput =new DataInputImpl(definitions);
                    parseAttributes(dataInput,child);
                    throwEvent.getDataInputs().add(dataInput);
                } if ( nodeName.equals("dataInputAssociation") && (flowElement instanceof ThrowEvent)){
                    ThrowEvent throwEvent=(ThrowEvent) flowElement;
                    List<DataInputAssociation> dataAssocList=throwEvent.getDataInputAssociations();
                    DataInputAssociation association = new DataInputAssociationImpl(definitions);
                    parseDataAssociation(association, child);
                    dataAssocList.add(association);
                } if (nodeName.equals("messageEventDefinition")&& (flowElement instanceof ThrowEvent)){
                    ThrowEvent throwEvent=(ThrowEvent) flowElement;
                    EventDefinition eventDefinition=new MessageEventDefinitionImpl(definitions);
                    parseAttributes(eventDefinition,child);
                    throwEvent.getEventDefinitions().add(eventDefinition);
                }
                
                /*
                   messageEventDefinition
                else if (nodeName.equals("extensionElements")){
                 parseExtensionElements(flowElement,XMLUtil.elements(child));
                 Element getFirstChildElement
                 } */

            }
        });
    }

    void parseResourceRoleElement(ResourceRole resourceRole, List<Element> childs) {
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            if (ns.equals(BPMN2_NS)) {
                if (nodeName.equals("resourceAssignmentExpression")) {
                    Element elem = XMLUtil.getFirstChildElement(child);
                    ResourceAssignmentExpression resourceAssignmentExpression = new ResourceAssignmentExpressionImpl(definitions);

                    if (elem != null) {
                        String ns1 = elem.getNamespaceURI();
                        String nodeName1 = elem.getLocalName();
                        if (ns1.equals(BPMN2_NS) && nodeName1.equals("formalExpression")) {
                            String script = XMLUtil.getContentText(elem);
                            FormalExpression formalExpression = new FormalExpressionImpl(definitions);
                            formalExpression.setBody(script);
                            resourceAssignmentExpression.setExpression(formalExpression);
                            resourceRole.setResourceAssignmentExpression(resourceAssignmentExpression);
                        }
                    }
                }
            }
        });
    }

    void parseRenderingElement(Rendering rendering, List<Element> childs) {
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
       
            if (ns.equals(BPMN2_NS) && nodeName.equals("extensionElements")) {
                List<Element> extensionchilds = XMLUtil.elements(child);
                extensionchilds.stream().forEach((extensionchild) -> {
                    String ns1 = extensionchild.getNamespaceURI();
                    String nodeName1 = extensionchild.getLocalName();
                    if (ns1.equals(XSYSTEMBPMN) && nodeName1.equals("formKey")) {
                        String formKey = XMLUtil.getContentText(extensionchild);
                        rendering.setFormKey(formKey);
                    }
                    if (ns1.equals(XSYSTEMBPMN) && nodeName1.equals("formContext")) {
                        String formContext = XMLUtil.getContentText(extensionchild);
                        rendering.setFormContext(formContext);
                    }
                });
            }
        });
    }

    /*void parseExtensionElements(FlowElement flowElement,List<Element> childs){
     if (flowElement instanceof UserTask){
     parseUserTaskExtension(flowElement,childs); 
     }
     }
    void parseUserTaskExtension(FlowElement flowElement, List<Element> childs) {
        Map extensions = (Map) flowElement.getExtensionElements();
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            if (ns.equals(XSYSTEMBPMN)) {
                if (nodeName.equals("form")) {
                    String form = XMLUtil.getContentText(child);

                    extensions.put("form", form);
                }
            }
        });
    }
     */
    void parseCorrelationPropertyBinding(CorrelationPropertyBinding correlationPropertyBinding, List<Element> childs) {
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            if (ns.equals(BPMN2_NS)) {
                if (nodeName.equals("dataPath")) {
                    FormalExpression dataPath = new FormalExpressionImpl(definitions);
                    parseAttributes(dataPath, child);
                    String value = XMLUtil.getContentText(child);
                    dataPath.setBody(value);
                    correlationPropertyBinding.setDataPath(dataPath);
                } else {
                    System.out.println("(CorrelationPropertyBinding not imp element " + nodeName);//correlationKeyRef
                }
            }
        });
    }

    void parseCorrelationSubscription(CorrelationSubscription correlationSubscription, List<Element> childs) {
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            if (ns.equals(BPMN2_NS)) {
                BaseElement baseElement = (BaseElement) BPMN2Factory.createBaseElement(nodeName, definitions);
                if (!(baseElement == null)) {
                    parseAttributes(baseElement, child);
                    if (baseElement instanceof CorrelationPropertyBinding) {
                        CorrelationPropertyBinding correlationPropertyBinding = (CorrelationPropertyBinding) baseElement;
                        correlationSubscription.getCorrelationPropertyBinding().add(correlationPropertyBinding);
                        parseCorrelationPropertyBinding(correlationPropertyBinding, XMLUtil.elements(child));
                    }
                } else if (nodeName.equals("correlationKeyRef")) {
                    Reference<CorrelationKey> ref = createReference(child);
                    correlationSubscription.setCorrelationKeyRef(ref);
                } else {
                    System.out.println("Subscription not imp element " + nodeName);//correlationKeyRef
                }
            }
        });
    }

    void parseLane(Lane lane, List<Element> childs) {
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            if (ns.equals(BPMN2_NS)) {
                switch (nodeName) {
                    case "flowNodeRef":
                        Reference ref = createReference(child);
                        lane.getFlowNodeRefs().add(ref);
                        break;
                    case "childLaneSet":
                        LaneSet laneSet = new LaneSetImpl(definitions);
                        parseAttributes(laneSet, child);
                        lane.setChildLaneSet(laneSet);
                        parseLaneSet(laneSet, XMLUtil.elements(child), lane);
                        break;
                    default:
                        //flowNodeRef
                        System.out.println("Lane not imp element " + nodeName);
                        break;
                }
            }
        });
    }

    void parseLaneSet(LaneSet laneSet, List<Element> childs, Lane parentLane) {
        laneSet.setParentLane(parentLane);
        laneSet.setProcess(currentProcess);
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            if (ns.equals(BPMN2_NS)) {
                BaseElement baseElement = (BaseElement) BPMN2Factory.createBaseElement(nodeName, definitions);
                if (!(baseElement == null)) {
                    parseAttributes(baseElement, child);
                    if (baseElement instanceof Lane) {
                        Lane lane = (Lane) baseElement;
                        laneSet.getLanes().add(lane);
                        parseLane(lane, XMLUtil.elements(child));
                    }
                } else {
                    System.out.println("LaneSet not imp element " + nodeName);
                }
            }
        });
    }

    void parseProcess(Process process, List<Element> childs) {
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
//                    getNodeName();
            if (ns.equals(BPMN2_NS)) {
                BaseElement baseElement = (BaseElement) BPMN2Factory.createBaseElement(nodeName, definitions);
                if (!(baseElement == null)) {
                    parseAttributes(baseElement, child);
                    if (baseElement instanceof FlowElement) {
                        FlowElement flowElement = (FlowElement) baseElement;
                        process.getFlowElements().add(flowElement);
                        parseFlowElement(flowElement, XMLUtil.elements(child));
                    } else if (baseElement instanceof CorrelationSubscription) {
                        CorrelationSubscription correlationSubscription = (CorrelationSubscription) baseElement;
                        process.getCorrelationSubscriptions().add(correlationSubscription);
                        parseCorrelationSubscription(correlationSubscription, XMLUtil.elements(child));
                    } else if (baseElement instanceof LaneSet) {
                        LaneSet laneSet = (LaneSet) baseElement;
                        process.getLaneSets().add(laneSet);
                        parseLaneSet(laneSet, XMLUtil.elements(child), null);
                    }
                } else {
                    System.out.println("Process not imp element " + nodeName);
                }
            }
        });
    }

    Reference createReference(String value) {
        String prfx = getPrefixNs(value);
        String local = getLocal(value);
        Reference ref;
        if (prfx == null) {
            ref = definitions.createReference(value);
        } else {
            String namespaceURI = definitions.getNamespaceURI(prfx);
            ref = definitions.createReference(namespaceURI, local, prfx);
        }
        return ref;
    }

    Reference createReference(Element child) {
        String value = XMLUtil.getContentText(child);
        return createReference(value);
    }

    QName createQName(String value) {
        String prfx = getPrefixNs(value);
        String local = getLocal(value);
        QName ret;
        if (prfx == null) {
            ret = new QName(local);
        } else {
            String namespaceURI = definitions.getNamespaceURI(prfx);
            ret = new QName(namespaceURI, local, prfx);
        }
        return ret;
    }

    void parseOperation(Operation operation, List<Element> childs
    ) {
        for (Element child : childs) {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            //getNodeName();
            if (ns.equals(BPMN2_NS)) {
                switch (nodeName) {
                    case "inMessageRef": {
                        Reference ref = createReference(child);
                        operation.setInMessageRef(ref);
                        break;
                    }
                    case "outMessageRef": {
                        Reference ref = createReference(child);
                        operation.setOutMessageRef(ref);
                        break;
                    }
                }
            }
        }
    }

    // inMessageRef   outMessageRef
    void parseInterface(Interface inter, List<Element> childs
    ) {
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            //getNodeName();
            if (ns.equals(BPMN2_NS)) {
                BaseElement baseElement = (BaseElement) BPMN2Factory.createBaseElement(nodeName, definitions);
                parseAttributes(baseElement, child);
                if (baseElement instanceof Operation) {
                    Operation operation = (Operation) baseElement;
                    inter.getOperations().add(operation);
                    parseOperation(operation, XMLUtil.elements(child));
                }
            }
        });
    }

    void parseCorrelationPropertyRetrievalExpression(CorrelationPropertyRetrievalExpression correlationPropertyRetrievalExpression, List<Element> childs
    ) {
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            if (ns.equals(BPMN2_NS) && nodeName.equals("messagePath")) {
                FormalExpression messagePath = new FormalExpressionImpl(definitions);
                parseAttributes(messagePath, child);
                String value = XMLUtil.getContentText(child);
                messagePath.setBody(value);
                correlationPropertyRetrievalExpression.setMessagePath(messagePath);
            }
        });
    }

    void parseCorrelationProperty(CorrelationProperty correlationProperty, List<Element> childs
    ) {
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            if (ns.equals(BPMN2_NS)) {
                BaseElement baseElement = (BaseElement) BPMN2Factory.createBaseElement(nodeName, definitions);
                if (baseElement != null) {
                    parseAttributes(baseElement, child);
                    if (baseElement instanceof CorrelationPropertyRetrievalExpression) {
                        CorrelationPropertyRetrievalExpression correlationPropertyRetrievalExpression = (CorrelationPropertyRetrievalExpression) baseElement;
                        correlationProperty.getCorrelationPropertyRetrievalExpressions().add(correlationPropertyRetrievalExpression);
                        parseCorrelationPropertyRetrievalExpression(correlationPropertyRetrievalExpression, XMLUtil.elements(child));
//correlationPropertyRetrievalExpression 
                    }
                }
            }
        });
    }

    void parseCorrelationKey(CorrelationKey correlationKey, List<Element> childs
    ) {
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            if (ns.equals(BPMN2_NS)) {
                if (nodeName.equals("correlationPropertyRef")) {
                    Reference<CorrelationProperty> correlationProperty = createReference(child);
                    correlationKey.getCorrelationProperty().add(correlationProperty);
                } else {
                    System.out.println("orrelationKey not imp element " + nodeName);
                }
            }
        });
    }

    void parseConversation(Conversation conversation, List<Element> childs
    ) {
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            if (ns.equals(BPMN2_NS)) {
                BaseElement baseElement = (BaseElement) BPMN2Factory.createBaseElement(nodeName, definitions);
                if (baseElement != null) {
                    parseAttributes(baseElement, child);
                    if (baseElement instanceof CorrelationKey) {
                        CorrelationKey correlationKey = (CorrelationKey) baseElement;
                        conversation.getCorrelationKeys().add(correlationKey);
                        parseCorrelationKey(correlationKey, XMLUtil.elements(child));
                    }
                } else if (nodeName.equals("messageFlowRef")) {
                    Reference<MessageFlow> mesageFlow = createReference(child);
                    conversation.getMessageFlowRefs().add(mesageFlow);
                    //messageFlowRef
                } else {
                    System.out.println("Conversation not imp element " + nodeName);
                }
            }
        });
    }

    void parseParticipant(Participant participant, List<Element> childs){
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            if (ns.equals(BPMN2_NS)) {
                BaseElement baseElement = (BaseElement) BPMN2Factory.createBaseElement(nodeName, definitions);
                if (baseElement!=null){
                    parseAttributes(baseElement, child);
                    if (baseElement instanceof ParticipantMultiplicity){
                       ParticipantMultiplicity participantMultiplicity=(ParticipantMultiplicity) baseElement;
                       participant.setParticipantMultiplicity(participantMultiplicity);
                    }
                }
            }
        });    
    }
    
    void parseCollaboration(Collaboration collaboration, List<Element> childs
    ) {
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            if (ns.equals(BPMN2_NS)) {
                BaseElement baseElement = (BaseElement) BPMN2Factory.createBaseElement(nodeName, definitions);

                if (baseElement != null) {
                    parseAttributes(baseElement, child);

                    //participant
                    if (baseElement instanceof Participant) {
                        Participant participant = (Participant) baseElement;
                        parseParticipant(participant, XMLUtil.elements(child));
                        collaboration.getParticipants().add(participant);
                    } else if (baseElement instanceof MessageFlow) {
                        MessageFlow messageFlow = (MessageFlow) baseElement;
                        collaboration.getMessageFlow().add(messageFlow);
                    } else if (baseElement instanceof Conversation) {
                        Conversation conversation = (Conversation) baseElement;
                        collaboration.getConversations().add(conversation);

                        parseConversation(conversation, XMLUtil.elements(child));
                    }
                } else {
                    System.out.println("Collaboration not imp element " + nodeName);
                }
            }
        });
    }

    void parseRoot(Element rootElement
    ) {
        String nodeName = rootElement.getLocalName();
        //getNodeName();
        RootElement root = (RootElement) BPMN2Factory.createBaseElement(nodeName, definitions);
        parseAttributes(root, rootElement);
        definitions.getRootElements().add(root);
        if (root instanceof Process) {
            Process process = (Process) root;
            currentProcess = process;
            List<Element> childs = XMLUtil.elements(rootElement);
            parseProcess(process, childs);
        } else if (root instanceof Interface) {
            Interface inter = (Interface) root;
            List<Element> childs = XMLUtil.elements(rootElement);
            parseInterface(inter, childs);
        } else if (root instanceof CorrelationProperty) {
            CorrelationProperty correlationProperty = (CorrelationProperty) root;
            List<Element> childs = XMLUtil.elements(rootElement);
            parseCorrelationProperty(correlationProperty, childs);
        } else if (root instanceof Collaboration) {
            Collaboration collaboration = (Collaboration) root;
            List<Element> childs = XMLUtil.elements(rootElement);
            parseCollaboration(collaboration, childs);
        }
    }

    void parseBPMNLabel(BPMNLabel label, Element rootElement
    ) {
        List<Element> childs = XMLUtil.elements(rootElement);
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            //  getNodeName();
            if (ns.equals(DC_NS)) {
                if (BPMNDIFactory.isBounds(nodeName)) {
                    Bounds bounds = new Bounds();
                    parseAttributes(bounds, child);
                    label.setBounds(bounds);
                }
            }
        });
    }

    void parseBPMNShape(BPMNShape shape, Element rootElement
    ) {
        List<Element> childs = XMLUtil.elements(rootElement);
        for (Element child : childs) {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            //     getNodeName();
            switch (ns) {
                case DC_NS: {
                    if (BPMNDIFactory.isBounds(nodeName)) {
                        Bounds bounds = new Bounds();
                        parseAttributes(bounds, child);
                        shape.setBounds(bounds);
                    }
                }
                break;
                case BPMNDI_NS: {
                    DiagramElement diagramElement = BPMNDIFactory.createBPMNElement(nodeName);
                    parseAttributes(diagramElement, child);
                    if (diagramElement instanceof BPMNLabel) {
                        BPMNLabel label = (BPMNLabel) diagramElement;
                        parseBPMNLabel(label, child);
                        shape.setBpmnLabel(label);
                    }
                }
                break;
            }
        }
    }

    void parseBPMNEdge(BPMNEdge edge, Element rootElement
    ) {
        List<Element> childs = XMLUtil.elements(rootElement);
        for (Element child : childs) {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            //getNodeName();
            switch (ns) {
                case DI_NS: {
                    if (BPMNDIFactory.iswaypoint(nodeName)) {
                        Point point = new Point();
                        parseAttributes(point, child);
                        edge.getWaypoints().add(point);
                    }
                }
                break;
                case BPMNDI_NS: {
                    DiagramElement diagramElement = BPMNDIFactory.createBPMNElement(nodeName);
                    parseAttributes(diagramElement, child);
                    if (diagramElement instanceof BPMNLabel) {
                        BPMNLabel label = (BPMNLabel) diagramElement;
                        parseBPMNLabel(label, child);
                    }
                }
                break;
            }
        }
    }

    void parseBPMNPlane(BPMNDiagram diagram, Element rootElement
    ) {
        BPMNPlane bpmnPlane = new BPMNPlane();
        parseAttributes(bpmnPlane, rootElement);
        diagram.setBPMNPlane(bpmnPlane);
        List<Element> childs = XMLUtil.elements(rootElement);
        childs.stream().forEach((child) -> {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            //getNodeName();
            if (ns.equals(BPMNDI_NS)) {
                DiagramElement diagramElement = BPMNDIFactory.createBPMNElement(nodeName);
                parseAttributes(diagramElement, child);
                if (diagramElement instanceof BPMNShape) {
                    BPMNShape shape = (BPMNShape) diagramElement;
                    bpmnPlane.getDiagramElements().add(shape);
                    parseBPMNShape(shape, child);
                } else if (diagramElement instanceof BPMNEdge) {
                    BPMNEdge edge = (BPMNEdge) diagramElement;
                    bpmnPlane.getDiagramElements().add(edge);
                    parseBPMNEdge(edge, child);
                }
            }
        });
    }

    void parseDiagram(Element rootElement
    ) {
        BPMNDiagram diagram = new BPMNDiagram();
        parseAttributes(diagram, rootElement);
        definitions.getBPMNDiagrams().add(diagram);
        List<Element> childs = XMLUtil.elements(rootElement);
        for (Element child : childs) {
            String ns = child.getNamespaceURI();
            String nodeName = child.getLocalName();
            //getNodeName();
            if (ns.equals(BPMNDI_NS)) {
                if (BPMNDIFactory.isBPMNPlane(nodeName)) {
                    parseBPMNPlane(diagram, child);
                    return;
                }
            }
        }
    }

    @Override
    public Definitions parse(InputStream input) {
        try {
            Document document = validate(input);
            definitions = new DefinitionsImpl();
            ConvertUtils.register(new EnumConverter(), ProcessType.class);
            ConvertUtils.register(new EnumConverter(), GatewayDirection.class);
            Element rootElement = document.getDocumentElement();
            setNameSpace(rootElement);
            parseAttributes(definitions, rootElement);

            List<Element> childs = XMLUtil.elements(rootElement);
            childs.stream().forEach((child) -> {
                String ns = child.getNamespaceURI();
                String nodeName = child.getLocalName();
                // getNodeName();
                switch (ns) {
                    case BPMN2_NS:
                        if (BPMN2Factory.isRootElement(nodeName)) {
                            parseRoot(child);
                        } else if (BPMN2Factory.isImport(nodeName)) {
                            parseImport(child);
                        }
                        break;
                    case BPMNDI_NS:
                        if (nodeName.equals("BPMNDiagram")) {
                            parseDiagram(child);
                        }
                        break;
                }
            });
            definitions.prepare();
            return definitions;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            throw new java.lang.Error(ex);
        }
    }
}
