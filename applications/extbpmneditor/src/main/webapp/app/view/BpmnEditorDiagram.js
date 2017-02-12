/* 
 * Copyright (C) 2017 Andrey Timofeev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

Ext.define('BPMN.view.BpmnEditorDiagram', {
    extend: 'Ext.Panel',
    alias: 'widget.BPMN-BpmnEditorDiagram',
    width: 200,
    initComponent: function () {
        
        var self = this;
        self.html = '<div id="myDiagramDiv" style="border: solid 1px gray;height: 100%;width:100% "></div>';
     //   self.emptymodel={"class":"go.GraphLinksModel","linkKeyProperty":"key","nodeDataArray":[],"linkDataArray":[]};
        self.model={"class":"go.GraphLinksModel","linkKeyProperty":"key","nodeDataArray":[],"linkDataArray":[]};
                
        
        self.bpmnDiagram = new XS.graphObject.BpmnDiagram({
            diagramDiv: "myDiagramDiv",
            selectionCb:function(data,isDiagram){
                BPMN.app.fireEvent('DiagramChangedSelection',data,isDiagram);
               // console.log(data);
            },
            portselectionCb:function(data){
                BPMN.app.fireEvent('PortDiagramSelection',data);
            }
        });
        
        self.listeners = {
            afterrender: function () {
               self.bpmnDiagram.render();
               if (self.model){
                  self.setModel(self.model); 
               }
            },
            resize: {
                  fn: function(el) {
                      self.bpmnDiagram.doLayout();
                  }
              }
        };

        this.callParent(arguments);
        
        
    },
    toJson:function (){
       var ret=this.bpmnDiagram.toJson();
       return ret;
    },
    setModel:function(model){
       this.model=model; 
       this.bpmnDiagram.setModel(model);
       
       var selection=this.getSelection();
       BPMN.app.fireEvent('DiagramChangedSelection',selection);
    },
    
    newDiagram:function(){
        var newmodel={"class":"go.GraphLinksModel","linkKeyProperty":"key","nodeDataArray":[],"linkDataArray":[]};
        this.setModel(newmodel);
    },
    
    getSelection:function(){
      if (this.bpmnDiagram){
        return this.bpmnDiagram.getSelection();
        
      }  
    }
});
