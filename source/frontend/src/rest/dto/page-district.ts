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
import { PageNumber } from './page-number';
import { PageSize } from './page-size';

/**
 * 
 * @export
 * @interface PageDistrict
 */
export interface PageDistrict {
    /**
     * 
     * @type {Array<District>}
     * @memberof PageDistrict
     */
    elements: Array<District>;
    /**
     * 
     * @type {PageNumber}
     * @memberof PageDistrict
     */
    pageNumber: PageNumber;
    /**
     * 
     * @type {PageSize}
     * @memberof PageDistrict
     */
    pageSize: PageSize;
    /**
     * 
     * @type {number}
     * @memberof PageDistrict
     */
    totalPages: number;
}


