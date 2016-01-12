<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://xmlns.oracle.com/adf/faces/rich" prefix="af"%>
<%@ taglib uri="http://xmlns.oracle.com/dss/adf/faces" prefix="dvt"%>
<f:view>
  <af:document id="d1">
    <af:messages id="m1"/>
    <af:form id="f1">
      <dvt:hierarchyViewer id="hv1" var="node"
                           value="#{bindings.VO_SupervisorOrgChart1.treeModel}"
                           selectionListener="#{bindings.VO_SupervisorOrgChart1.treeModel.makeCurrent}"
                           layout="hier_vert_top"
                           levelFetchSize="#{bindings.VO_SupervisorOrgChart1.rangeSize}"
                           styleClass="AFStretchWidth">
        <dvt:link linkType="orthogonalRounded" id="l1"/>
        <dvt:node type="hub.nishat.net.model.VO.Supervisor.VO_SupervisorOrgChart"
                  width="233" height="233" id="n1">
          <f:facet name="zoom100">
            <af:panelGroupLayout styleClass="AFStretchWidth AFHVNodeStretchHeight AFHVNodePadding"
                                 layout="horizontal" valign="middle" id="pgl1">
              <af:panelGroupLayout layout="vertical" halign="center"
                                   styleClass="AFStretchWidth" id="pgl2">
                <af:outputText value="#{node.HierarchyName}"
                               styleClass="AFHVNodeTitleTextStyle" id="ot1"/>
                <af:outputText value="#{node.ParentPositionId}"
                               styleClass="AFHVNodeSubtitleTextStyle" id="ot2">
                  <af:convertNumber groupingUsed="false"
                                    pattern="#{bindings.VO_SupervisorOrgChart1.hints.ParentPositionId.format}"/>
                </af:outputText>
                <af:spacer height="5" id="s1"/>
                <af:panelFormLayout styleClass="AFStretchWidth" id="pfl1">
                  <af:panelLabelAndMessage label="#{bindings.VO_SupervisorOrgChart1.hints.FullNameParent.label}"
                                           styleClass="AFHVNodeLabelStyle"
                                           id="plam1">
                    <af:outputText value="#{node.FullNameParent}"
                                   styleClass="AFHVNodeTextStyle" id="ot5"/>
                  </af:panelLabelAndMessage>
                  <af:panelLabelAndMessage label="#{bindings.VO_SupervisorOrgChart1.hints.ParentPersonId.label}"
                                           styleClass="AFHVNodeLabelStyle"
                                           id="plam3">
                    <af:outputText value="#{node.ParentPersonId}"
                                   styleClass="AFHVNodeTextStyle" id="ot3">
                      <af:convertNumber groupingUsed="false"
                                        pattern="#{bindings.VO_SupervisorOrgChart1.hints.ParentPersonId.format}"/>
                    </af:outputText>
                  </af:panelLabelAndMessage>
                  <af:panelLabelAndMessage label="#{bindings.VO_SupervisorOrgChart1.hints.ParentPosition.label}"
                                           styleClass="AFHVNodeLabelStyle"
                                           id="plam2">
                    <af:outputText value="#{node.ParentPosition}"
                                   styleClass="AFHVNodeTextStyle" id="ot4"/>
                  </af:panelLabelAndMessage>
                </af:panelFormLayout>
              </af:panelGroupLayout>
            </af:panelGroupLayout>
          </f:facet>
        </dvt:node>
      </dvt:hierarchyViewer>
    </af:form>
  </af:document>
</f:view>