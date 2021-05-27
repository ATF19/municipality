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
 * @interface RegisterRequest
 */
export interface RegisterRequest {
    /**
     * 
     * @type {string}
     * @memberof RegisterRequest
     */
    username: string;
    /**
     * 
     * @type {string}
     * @memberof RegisterRequest
     */
    email: string;
    /**
     * 
     * @type {string}
     * @memberof RegisterRequest
     */
    password: string;
    /**
     * 
     * @type {string}
     * @memberof RegisterRequest
     */
    firstName: string;
    /**
     * 
     * @type {string}
     * @memberof RegisterRequest
     */
    lastName: string;
    /**
     * 
     * @type {boolean}
     * @memberof RegisterRequest
     */
    isAdmin: boolean;
    /**
     * 
     * @type {Set<string>}
     * @memberof RegisterRequest
     */
    municipalitiesResponsible: Set<string>;
    /**
     * 
     * @type {Set<string>}
     * @memberof RegisterRequest
     */
    municipalitiesAuditor: Set<string>;
    /**
     * 
     * @type {Set<string>}
     * @memberof RegisterRequest
     */
    districtsResponsible: Set<string>;
    /**
     * 
     * @type {Set<string>}
     * @memberof RegisterRequest
     */
    districtsAuditor: Set<string>;
}


