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


import { ComplaintDto } from './complaint-dto';
import { PageComplaint } from './page-complaint';

/**
 * 
 * @export
 * @interface PageDtoComplaintComplaintDto
 */
export interface PageDtoComplaintComplaintDto {
    /**
     * 
     * @type {PageComplaint}
     * @memberof PageDtoComplaintComplaintDto
     */
    page?: PageComplaint;
    /**
     * 
     * @type {object}
     * @memberof PageDtoComplaintComplaintDto
     */
    mapper?: object;
    /**
     * 
     * @type {number}
     * @memberof PageDtoComplaintComplaintDto
     */
    number: number;
    /**
     * 
     * @type {number}
     * @memberof PageDtoComplaintComplaintDto
     */
    size: number;
    /**
     * 
     * @type {number}
     * @memberof PageDtoComplaintComplaintDto
     */
    totalPages: number;
    /**
     * 
     * @type {Array<ComplaintDto>}
     * @memberof PageDtoComplaintComplaintDto
     */
    elements: Array<ComplaintDto>;
}


