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


import { ArticleId } from './article-id';
import { Title } from './title';

/**
 * 
 * @export
 * @interface ArticleWithoutContent
 */
export interface ArticleWithoutContent {
    /**
     * 
     * @type {ArticleId}
     * @memberof ArticleWithoutContent
     */
    id: ArticleId;
    /**
     * 
     * @type {Title}
     * @memberof ArticleWithoutContent
     */
    title: Title;
    /**
     * 
     * @type {string}
     * @memberof ArticleWithoutContent
     */
    createdAt?: string;
    /**
     * 
     * @type {string}
     * @memberof ArticleWithoutContent
     */
    createdBy?: string;
}


