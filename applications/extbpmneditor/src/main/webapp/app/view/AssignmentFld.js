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

Ext.define('BPMN.view.AssignmentFld', {
    extend: 'Ext.form.field.Text',
    requires: ['BPMN.view.AssignmentEditor'],
    // triggerBaseCls: 'x-form-clear-trigger',
    alias: 'widget.BPMN-AssignmentFld',
    triggers: {
        foo: {
            //   cls: 'my-foo-trigger',
            handler: function (fld) {
                var editor = Ext.create('BPMN.view.AssignmentEditor',
                        {width: 400
                            , height: 400
                            ,saveCb:fld.saveCb
                        });
                var v = fld.getValue();
                v=Ext.decode(v);
                v=fld.helper(v) 
               // console.log(v);
                editor.setValue(v);
                editor.show();
            }
        }},
    initComponent: function () {

        this.callParent(arguments);

    },
    
    helper:function(src){
       var tgt=src;
       var inspectedObject=this.getModel();
       var modelData=inspectedObject.data;
       if (modelData.type==="SendTask"&& modelData.message ){
            var mes=modelData.message;
            var mesStruct=this.getMessage(mes,inspectedObject);
            tgt = this.buildTgtFromSend(tgt,mesStruct);
       }
       return tgt;
    },
    
    getMessage:function(mes,inspectedObject){
        var diagram=inspectedObject.diagram;
       var modelData=diagram.model.modelData;
       var messages=modelData.messages||{};
       var message=messages[mes]||{};
       return message;
    },
    
    buildTgtFromSend:function(tgt,mesStruct){
        var messagePath=mesStruct.messagePath||{};
        for (var path in messagePath) {
             if (messagePath.hasOwnProperty(path)) {
                  var tovalue=messagePath[path];
                  var ok=tgt.find(function(tgtelem){
                      if (tgtelem.to===tovalue){
                         return true;
                      }   
                      return false;
                  })
                   if (!ok){
                      var row={to:tovalue};
                      tgt.push(row);
                  }
             }
         }     
         return tgt;
    }
    
});

