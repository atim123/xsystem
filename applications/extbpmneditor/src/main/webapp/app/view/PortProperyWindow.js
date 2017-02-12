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

Ext.define('BPMN.view.PortProperyWindow', {
    extend: 'Ext.window.Window',
//   requires: ['BPMN.view.AssignmentGrid'],
    requires: ['Ext.grid.property.Grid'],
    frame: true,
    layout: 'fit',
    alias: 'widget.BPMN-PortProperyWindow',
    width: 500,
    esizable: true,
    height: 400,
    modal: true,
    initComponent: function () {
        var self = this;
        
        self.buttons = [
            {
                text: 'Закрыть',
                scope: self,
                handler: self.close
            }
        ];
        
        var source = {};

        self.sourceConfig = {
            type: {
                editor: Ext.create('Ext.form.DisplayField')
            }
        };
        
        self.grid = Ext.create('Ext.grid.property.Grid', {
            sortableColumns: false,
            source: source,
            sourceConfig: self.sourceConfig,
            listeners: {
                propertychange: function (source, recordId, value, oldValue) {
                   self.changeProperty(recordId, value, oldValue);
                }
            }
        });
        
        self.items = [self.grid];
        
        this.callParent(arguments);   
        
        
    },
    
    setSimpleProp: function (src, name, value) {
        if (value) {
            src[name] = value;
        } else {
            src[name] = null;
        }
    },
    
    changeVtime: function (diagram, modelData, newValue) {
        var model = diagram.model;
        model.setDataProperty(modelData, "time", newValue);
    },
    
    changeProperty: function (name, newValue, oldValue) {
        var inspectedObject = this.inspectedObject;
        var modelData = this.inspectedObject.panel.data;
        var diagram=inspectedObject.diagram;
        diagram.startTransaction('set all properties');
        var fn = "changeV" + name;
        if (this[fn]) {
            this[fn](diagram, modelData, newValue, oldValue);
        }
        diagram.commitTransaction('set all properties');
    },
    
    setBoundaryTimerEvent:function (source){
        var modelData = this.inspectedObject.panel.data;
        var time = modelData.time;
        this.setSimpleProp(source, "time", time);
        this.grid.setSource(source);
    },
    
    setData: function (inspectedObject) {
       // console.log(inspectedObject.panel.data);
        this.inspectedObject = inspectedObject;
        var source={type:inspectedObject.panel.data.type};
  
        var fn = "set" + source.type;
        console.log(fn);
        if (this[fn]) {
            this[fn](source);
        }    
    }    
});
