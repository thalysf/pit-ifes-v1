import { Resource } from "../model/resource";

export interface ResourceSchemaMap {
    [resource: string]: Resource;
}

export abstract class SchemaService {
    resourceSchemaMap!: ResourceSchemaMap;

    constructor(resourceSchemaMap: ResourceSchemaMap){
        this.resourceSchemaMap = resourceSchemaMap;
    }

    public getResource(resource: string): Resource {
        return this.resourceSchemaMap[resource];
    }
}
