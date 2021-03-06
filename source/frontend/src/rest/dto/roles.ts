/* tslint:disable */
/* eslint-disable */
/**
 * Municipality API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */



/**
 * 
 * @export
 * @interface Roles
 */
export interface Roles {
    /**
     * 
     * @type {Set<object>}
     * @memberof Roles
     */
    listOfRoles?: Set<object>;
    /**
     * 
     * @type {boolean}
     * @memberof Roles
     */
    admin: boolean;
    /**
     * 
     * @type {boolean}
     * @memberof Roles
     */
    municipalityResponsible: boolean;
    /**
     * 
     * @type {boolean}
     * @memberof Roles
     */
    municipalityAuditor: boolean;
    /**
     * 
     * @type {boolean}
     * @memberof Roles
     */
    districtResponsible: boolean;
    /**
     * 
     * @type {boolean}
     * @memberof Roles
     */
    districtAuditor: boolean;
}


