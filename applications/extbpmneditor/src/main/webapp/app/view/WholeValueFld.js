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

Ext.define('BPMN.view.WholeValueFld', {
    extend: 'Ext.form.field.TextArea',//Ext.form.field.Text',
    requires: ['BPMN.view.WholeValueEditor'],
    // triggerBaseCls: 'x-form-clear-trigger',
    alias: 'widget.BPMN-WholeValueFld',
    
    triggers :{
            foo: {
             //   cls: 'my-foo-trigger',
                handler: function (fld) {
                    var width=(fld.width_win)?fld.width_win:200;
                    var height=(fld.height_win)?fld.height_win:200;
                    
                    var editor=Ext.create('BPMN.view.WholeValueEditor',
                    {  width: width
                      ,height: height
                      ,saveCb:fld.saveCb
                    });
                    var v=fld.getValue();
                    editor.setValue(v);
                    editor.show(); 
                }
            }},
            
    initComponent: function () {
        
        this.callParent(arguments);

    }
});



