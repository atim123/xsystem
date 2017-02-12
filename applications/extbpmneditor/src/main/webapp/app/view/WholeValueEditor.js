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

Ext.define('BPMN.view.WholeValueEditor', {
    
    extend: 'Ext.window.Window'
    ,requires: ['BPMN.view.TextEditor']
    ,frame: true
    , layout: 'fit'//border'
    , alias: 'widget.BPMN-WholeValueEditor'
    , width: 500
    ,resizable: true
    , height: 400
    ,modal:true
    //, constrainHeader: true
    , initComponent: function () {

        var self = this;
        var varBody = Ext.create("BPMN.view.TextEditor", {}); 
       /* 
        var varBody = new Ext.form.TextArea({
            region: 'center'
            ,
            height: 350
            ,
            split: true
            ,
            validateOnBlur: false
        });
        */

        this.items = [varBody];

        this.buttons = [
            {
                text: 'ОК',
                handler:function(){
                    if (self.saveCb){
                        self.saveCb(self.getValue());
                    }
                    self.close();
                }
             },
            {
                text: 'Отменить',
                scope: this,
                handler: this.close
            }
        ];

        this.callParent(arguments);


        this.setValue = function (o) {
            varBody.setValue(o);
        };

        this.getValue = function () {
            var ret = varBody.getValue();
            return ret;
        };
    }
});


