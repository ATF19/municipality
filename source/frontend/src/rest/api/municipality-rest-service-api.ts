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


import globalAxios, { AxiosPromise, AxiosInstance } from 'axios';
import { Configuration } from '../configuration';
// Some imports not used depending on template conditions
// @ts-ignore
import { DUMMY_BASE_URL, assertParamExists, setApiKeyToObject, setBasicAuthToObject, setBearerAuthToObject, setOAuthToObject, setSearchParams, serializeDataIfNeeded, toPathString, createRequestFunction } from '../common';
// @ts-ignore
import { BASE_PATH, COLLECTION_FORMATS, RequestArgs, BaseAPI, RequiredError } from '../base';
// @ts-ignore
import { PageDtoMunicipalityMunicipalityDto } from '../dto';
/**
 * MunicipalityRestServiceApi - axios parameter creator
 * @export
 */
export const MunicipalityRestServiceApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * 
         * @param {number} [page] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        all1: async (page?: number, options: any = {}): Promise<RequestArgs> => {
            const localVarPath = `/municipality`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            if (page !== undefined) {
                localVarQueryParameter['page'] = page;
            }


    
            setSearchParams(localVarUrlObj, localVarQueryParameter, options.query);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
    }
};

/**
 * MunicipalityRestServiceApi - functional programming interface
 * @export
 */
export const MunicipalityRestServiceApiFp = function(configuration?: Configuration) {
    const localVarAxiosParamCreator = MunicipalityRestServiceApiAxiosParamCreator(configuration)
    return {
        /**
         * 
         * @param {number} [page] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async all1(page?: number, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<PageDtoMunicipalityMunicipalityDto>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.all1(page, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
    }
};

/**
 * MunicipalityRestServiceApi - factory interface
 * @export
 */
export const MunicipalityRestServiceApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    const localVarFp = MunicipalityRestServiceApiFp(configuration)
    return {
        /**
         * 
         * @param {number} [page] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        all1(page?: number, options?: any): AxiosPromise<PageDtoMunicipalityMunicipalityDto> {
            return localVarFp.all1(page, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * MunicipalityRestServiceApi - interface
 * @export
 * @interface MunicipalityRestServiceApi
 */
export interface MunicipalityRestServiceApiInterface {
    /**
     * 
     * @param {number} [page] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof MunicipalityRestServiceApiInterface
     */
    all1(page?: number, options?: any): AxiosPromise<PageDtoMunicipalityMunicipalityDto>;

}

/**
 * MunicipalityRestServiceApi - object-oriented interface
 * @export
 * @class MunicipalityRestServiceApi
 * @extends {BaseAPI}
 */
export class MunicipalityRestServiceApi extends BaseAPI implements MunicipalityRestServiceApiInterface {
    /**
     * 
     * @param {number} [page] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof MunicipalityRestServiceApi
     */
    public all1(page?: number, options?: any) {
        return MunicipalityRestServiceApiFp(this.configuration).all1(page, options).then((request) => request(this.axios, this.basePath));
    }
}
