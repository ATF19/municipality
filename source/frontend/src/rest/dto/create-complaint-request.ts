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


import { PersonalInfo } from './personal-info';
import { Position } from './position';

/**
 * 
 * @export
 * @interface CreateComplaintRequest
 */
export interface CreateComplaintRequest {
    /**
     * 
     * @type {string}
     * @memberof CreateComplaintRequest
     */
    address: string;
    /**
     * 
     * @type {string}
     * @memberof CreateComplaintRequest
     */
    comment?: string;
    /**
     * 
     * @type {Position}
     * @memberof CreateComplaintRequest
     */
    position?: Position;
    /**
     * 
     * @type {PersonalInfo}
     * @memberof CreateComplaintRequest
     */
    personalInfo?: PersonalInfo;
}


