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


import { District } from './district';
import { MunicipalityId } from './municipality-id';
import { MunicipalityName } from './municipality-name';
import { MunicipalityNameInArabic } from './municipality-name-in-arabic';

/**
 * 
 * @export
 * @interface Municipality
 */
export interface Municipality {
    /**
     * 
     * @type {MunicipalityId}
     * @memberof Municipality
     */
    id: MunicipalityId;
    /**
     * 
     * @type {number}
     * @memberof Municipality
     */
    version: number;
    /**
     * 
     * @type {string}
     * @memberof Municipality
     */
    createdAt?: string;
    /**
     * 
     * @type {string}
     * @memberof Municipality
     */
    modifiedAt?: string;
    /**
     * 
     * @type {string}
     * @memberof Municipality
     */
    createdBy?: string;
    /**
     * 
     * @type {string}
     * @memberof Municipality
     */
    updatedBy?: string;
    /**
     * 
     * @type {MunicipalityName}
     * @memberof Municipality
     */
    name: MunicipalityName;
    /**
     * 
     * @type {MunicipalityNameInArabic}
     * @memberof Municipality
     */
    nameInArabic: MunicipalityNameInArabic;
    /**
     * 
     * @type {Array<District>}
     * @memberof Municipality
     */
    districts: Array<District>;
}

