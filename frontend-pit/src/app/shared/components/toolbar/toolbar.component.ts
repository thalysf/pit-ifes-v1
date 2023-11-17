import {Component, Input, NgModule, OnChanges, OnInit, SimpleChanges} from "@angular/core";
import {CommonModule, Location} from "@angular/common";
import {DxButtonModule, DxToolbarModule} from "devextreme-angular";
import {Observable} from "rxjs";
import {DxiToolbarItemModule} from "devextreme-angular/ui/nested";
import {BrowserModule} from "@angular/platform-browser";
import {tap} from 'rxjs/operators';


export interface ActionToolbar {
    text: string,
    icon: string,
    type: 'success' | 'default' | 'danger',
    onClick: Observable<any>
}

@Component({
    selector: 'toolbar',
    templateUrl: 'toolbar.component.html',
    styleUrls: ['toolbar.component.css']
})
export class ToolbarComponent implements OnInit, OnChanges {
    @Input() disableBefore = false;
    @Input() titulo!: string;
    @Input() back: Observable<any> | null = null;
    @Input() actions: any[] = [];

    backOption: any;
    actionsAposConfigurar: ActionToolbar[] = [];

    constructor(private location: Location) {
        this.backOption = {
            onClick: this.backFunction,
            icon: 'back',
            classList: 'btn-back'
        }
    }

    ngOnInit(): void {
        // this.configurarActions();
        // this.configurarActions();
    }

    ngOnChanges(changes: SimpleChanges) {
        if(changes['actions']){
            this.configurarActions();
        }
    }

    configurarActions = () => {
        this.actionsAposConfigurar = [];
        this.actions.forEach(action => {
            this.actionsAposConfigurar.push({
                ...action,
                onClick: ($button: any) => this.beforeExecuteAction($button, action.onClick)
            })
        })
    }

    backFunction = () => {
        if (this.back) this.back.subscribe();
        else {
            this.location.back();
        }
    }

    beforeExecuteAction(button: any, action: Observable<any>) {
        const icon = button.component.option('icon');
        button.component.option({disabled: true, icon: 'zmdi zmdi-spinner zmdi-hc-spin'});

        action.pipe(tap(
            response => {
                button.component.option({disabled: false, icon: icon});
            }, error => {
                button.component.option({disabled: false, icon: icon});
            }
        )).subscribe();
    }
}

@NgModule({
    imports: [
        CommonModule,
        BrowserModule,
        DxToolbarModule,
        DxButtonModule,
        DxiToolbarItemModule,
    ],
    declarations: [ToolbarComponent],
    exports: [ToolbarComponent]
})
export class ToolbarModule {
}
