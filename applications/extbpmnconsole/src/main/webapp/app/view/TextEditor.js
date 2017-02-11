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

Ext.define('BPMN.view.TextEditor', {
    extend: 'Ext.Panel',
    alias: 'widget.BPMN-textEditor',
    width: 200,
    initComponent: function () {
        var self = this;
        //self.scrollable=true;
        //self.html = '<textarea></textarea>';
        //self.html = '<textarea></textarea>';
        self.html = '<form><textarea></textarea></form>';
        self.listeners = {
            afterrender: function () {

                if (!self.codeMirror) {
                    var dom = this.getEl().down('textarea').dom;
                    self.codeMirror = CodeMirror.fromTextArea(dom, {
                        lineNumbers: true,
                        theme: 'night'//'default'
                                //viewportMargin:Infinity 
                    });
                    if (self.tmpData){
                       self.codeMirror.setValue(self.tmpData);
                       self.tmpData="";
                    }
                    // self.codeMirror.setSize(60,60);
                }
            },
            resize: function (el,w,h) {
                self.codeMirror.setSize(w,h)
            }

        }

        this.callParent(arguments);
    },
    getValue: function () {
        return this.getValue();
    },
    setValue: function (value) {
        this.setValue(value);
    },
    setData: function (data) {
        var cm = this.getCodeMirror();
        if (cm){
          cm.setValue(data);
        } else {
            this.tmpData=data;
        }
    },
    getData: function () {
        var cm = this.getCodeMirror();
        var ret;
        if (cm){
          ret= cm.getValue();
        } else {
          ret=this.tmpData;  
        }  
        return ret;
    },
    /*Overriding setValue function of the field to put the value in the code mirror window*/
    
    getCodeMirror: function () {
        return this.codeMirror;
    }

});


