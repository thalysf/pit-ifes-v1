
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';
import { SchemaService } from './schema-service';
import { Resource } from '../model/resource';
import { Observable, EMPTY, of } from 'rxjs';


@Injectable()
export class ResourceSchemaResolve implements Resolve<Resource> {

    constructor(private router: Router, private schemaService: SchemaService) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Resource> {
        if (route?.parent?.paramMap.get('entity')) {
            let resource: Resource = route.parent.data['resourceSchema'];

            let reference = resource?.availableReferences.filter((r: any) => r.referenceField === route.paramMap.get('entity'))[0];

            if (!reference) {
                this.router.navigate(['/perfil']);
                return EMPTY;
            }

            return of(reference);
        } else {
            const schema = this.schemaService.getResource(route.paramMap.get('entity')!);
            if(!schema){
                this.router.navigate(['/perfil']);
                return EMPTY;
            }
            return of(schema);
        }
    }
}

@Injectable()
export class ParentResourceSchemaResolve implements Resolve<Resource> {

    constructor(private router: Router, private schemaService: SchemaService) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Resource> {
        const targetEntity = route.params['targetEntity'];
        const schema = this.schemaService.getResource(targetEntity);
        if(!schema){
            this.router.navigate(['/perfil']);
            return EMPTY;
        }
        return of(schema);
    }
}
