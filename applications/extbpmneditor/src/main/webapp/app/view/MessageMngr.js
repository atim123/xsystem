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

Ext.define('BPMN.view.MessageMngr', {
   extend: 'Ext.window.Window',
    frame: true,
    requires: ['BPMN.view.MessageGrid','BPMN.view.MessagePathGrid'],
    alias: 'widget.BPMN-MessageMngr',
    width: 500,
    esizable: true,
    height: 400, 
    layout: 'border',
    modal:true,
    initComponent: function () {
       var self=this;
       
       self.inspectedObject=this.getModel();
       
       self.messageGrid=Ext.create('BPMN.view.MessageGrid', {region: 'west',split: true,width: 300,owner:self,title:"messages"});
       self.pathGrid = Ext.create('BPMN.view.MessagePathGrid', {region: 'center',owner:self,title:"message [?]"});
       
       self.items =[self.messageGrid,self.pathGrid];
       
       
       
       self.buttons = [
            {
                text: 'ОК',
                action:"select", 
                scope: this,
                disabled: true,
                handler:function(){
                    if (self.saveCb){
                        self.saveCb(self.getValue());
                    }
                    self.close();
                }
             },
            {
                text: 'Отменить',
                scope: self,
                handler: self.close
            }
        ];
        this.callParent(arguments);
       
    },
    
    getValue:function(){
      return  this.pathGrid.mesage; 
    },
    
    getMessagesData:function(){
       var diagram=this.inspectedObject.diagram;
       var modelData=diagram.model.modelData;
       var ret=[]; 
       
       var messages=modelData.messages;
           for (var mes in messages){
              if (messages.hasOwnProperty(mes)){
                  var row={msgid:mes,text:messages[mes].text};
                  ret.push(row);
              } 
           }
      
       return ret;
    },
    
    getPathdata:function(mes){
       var pathGrid=this.pathGrid;
        
       
       pathGrid.mesage=mes;
       pathGrid.store.removeAll () ;
       
       var diagram=this.inspectedObject.diagram;
       var modelData=diagram.model.modelData;
       var ret=[];  
       if (mes){
         var message=modelData.messages[mes];
         var messagePath=message.messagePath ||(message.messagePath={});
         for (var id in messagePath){
             if (messagePath.hasOwnProperty(id)){
                 var row={pathid:id,to:messagePath[id]};
                 ret.push(row);
             }
         }
      } 
      
      var btn= this.down('BPMN-MessagePathGrid button[action=add]');
      btn.setDisabled(!mes);
      var title="message [?]";
      if (mes){
        title="message ["+mes+"]";  
      }
      pathGrid.setTitle(title);
      pathGrid.store.loadData(ret); 
      btn= this.down('button[action=select]');
      btn.setDisabled(!mes);
      return ret;
        
    },
    
    
    
    newMessage:function(store){
       var key=this.getUniqueKey(); 
       var row = store.createModel({});
       row.set('msgid',key);
       var diagram=this.inspectedObject.diagram;
       diagram.model.createBpmMessage(key);
       return row;
    },
    
    newMessagePath:function(mes,store){
       var key=this.getUniqueKey(); 
       var row = store.createModel({});
       row.set('pathid',key);
       var diagram=this.inspectedObject.diagram;
       diagram.model.createBpmMessagePath(mes,key);
       return row;
    },
    
    changeMessageKey:function(oldValue,newValue){
        var diagram=this.inspectedObject.diagram;
        diagram.model.changeMessageKey(oldValue,newValue);
        var title="message ["+newValue+"]";
        this.pathGrid.setTitle(title);
        this.pathGrid.mesage=newValue;
    },
    
    changeMessagePathKey:function(mes,oldValue,newValue){
        var diagram=this.inspectedObject.diagram;
        diagram.model.changeMessagePathKey(mes,oldValue,newValue);
    },
    
    setMessagePathValue:function(mes,pathKey,newValue){
        var diagram=this.inspectedObject.diagram;
        diagram.model.setMessagePathValue(mes,pathKey,newValue);
    },
   
    removeMessagePath:function(mes,pathKey){
        var diagram=this.inspectedObject.diagram;
        diagram.model.removeMessagePath(mes,pathKey);
    },
    
    removeMessage:function(mes){
        var diagram=this.inspectedObject.diagram;
        diagram.model.removeMessage(mes);
    },
    
    getUniqueKey:function(){
        var diagram=this.inspectedObject.diagram;
        var ret=diagram.model.makeUniqueKeyFunction(diagram.model);
        return ret;
    },
    
    checkUniqueKey:function(key){
       var diagram=this.inspectedObject.diagram;
        var ret=diagram.model.checkUniqueKey(diagram.model,key);
        return ret; 
    },
    
    setData:function(inspectedObject){
        var diagram=inspectedObject.diagram;
        
        console.log(diagram.model.makeUniqueKeyFunction);
        console.log(diagram.model.checkUniqueKey);
        
        console.log(diagram);
        var model=diagram.model;
        console.log(model);
        var modelData=model.modelData;
        console.log(modelData);
    }
});   