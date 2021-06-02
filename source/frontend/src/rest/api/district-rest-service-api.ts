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
import { PageDtoDistrictDistrictDto } from '../dto';
/**
 * DistrictRestServiceApi - axios parameter creator
 * @export
 */
export const DistrictRestServiceApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * 
         * @param {number} [page] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        allDistricts: async (page?: number, options: any = {}): Promise<RequestArgs> => {
            const localVarPath = `/district`;
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
 * DistrictRestServiceApi - functional programming interface
 * @export
 */
export const DistrictRestServiceApiFp = function(configuration?: Configuration) {
    const localVarAxiosParamCreator = DistrictRestServiceApiAxiosParamCreator(configuration)
    return {
        /**
         * 
         * @param {number} [page] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async allDistricts(page?: number, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<PageDtoDistrictDistrictDto>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.allDistricts(page, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
    }
};

/**
 * DistrictRestServiceApi - factory interface
 * @export
 */
export const DistrictRestServiceApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    const localVarFp = DistrictRestServiceApiFp(configuration)
    return {
        /**
         * 
         * @param {number} [page] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        allDistricts(page?: number, options?: any): AxiosPromise<PageDtoDistrictDistrictDto> {
            return localVarFp.allDistricts(page, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * DistrictRestServiceApi - interface
 * @export
 * @interface DistrictRestServiceApi
 */
export interface DistrictRestServiceApiInterface {
    /**
     * 
     * @param {number} [page] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof DistrictRestServiceApiInterface
     */
    allDistricts(page?: number, options?: any): AxiosPromise<PageDtoDistrictDistrictDto>;

}

/**
 * DistrictRestServiceApi - object-oriented interface
 * @export
 * @class DistrictRestServiceApi
 * @extends {BaseAPI}
 */
export class DistrictRestServiceApi extends BaseAPI implements DistrictRestServiceApiInterface {
    /**
     * 
     * @param {number} [page] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof DistrictRestServiceApi
     */
    public allDistricts(page?: number, options?: any) {
        return DistrictRestServiceApiFp(this.configuration).allDistricts(page, options).then((request) => request(this.axios, this.basePath));
    }
}
