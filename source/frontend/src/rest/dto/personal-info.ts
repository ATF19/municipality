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


import { Email } from './email';
import { FirstName } from './first-name';
import { LastName } from './last-name';
import { Phone } from './phone';

/**
 * 
 * @export
 * @interface PersonalInfo
 */
export interface PersonalInfo {
    /**
     * 
     * @type {FirstName}
     * @memberof PersonalInfo
     */
    firstName?: FirstName;
    /**
     * 
     * @type {LastName}
     * @memberof PersonalInfo
     */
    lastName?: LastName;
    /**
     * 
     * @type {Phone}
     * @memberof PersonalInfo
     */
    phone?: Phone;
    /**
     * 
     * @type {Email}
     * @memberof PersonalInfo
     */
    email?: Email;
}

