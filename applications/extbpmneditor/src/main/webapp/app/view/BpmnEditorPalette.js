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
Ext.define('BPMN.view.BpmnEditorPalette', {
    extend: 'Ext.Panel',
    alias: 'widget.BPMN-BpmnEditorPalette',
    //   title: 'Elements',
    layout: 'fit',
    width: 110,
    initComponent: function () {
        var self = this;
        self.html = '<div id="myPaletteDiv" style="border: solid 1px gray;height: 100%;width:100% "></div>';

        self.bpmnPalete = new XS.graphObject.BpmnPalete({paleteDiv: "myPaletteDiv"});
        self.listeners = {
            afterrender: function () {
               self.bpmnPalete.render();
                
            },
            resize: {
                  fn: function(el) {
                      self.bpmnPalete.doLayout();
                  }
              }
        };

        this.callParent(arguments);
//go.Overview, "myOverviewDiv"
//DiagramOverview
    }

});  