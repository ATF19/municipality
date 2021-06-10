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
import { ComplaintDto } from '../dto';
// @ts-ignore
import { ComplaintId } from '../dto';
// @ts-ignore
import { CreateComplaintRequest } from '../dto';
// @ts-ignore
import { PageDtoComplaintComplaintDto } from '../dto';
// @ts-ignore
import { UpdateComplaintRequest } from '../dto';
/**
 * ComplaintRestServiceApi - axios parameter creator
 * @export
 */
export const ComplaintRestServiceApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * 
         * @param {number} [page] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        allComplaints: async (page?: number, options: any = {}): Promise<RequestArgs> => {
            const localVarPath = `/complaint/all`;
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
        /**
         * 
         * @param {string} complaintCode 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        complaintByCode: async (complaintCode: string, options: any = {}): Promise<RequestArgs> => {
            // verify required parameter 'complaintCode' is not null or undefined
            assertParamExists('complaintByCode', 'complaintCode', complaintCode)
            const localVarPath = `/complaint/code/{complaintCode}`
                .replace(`{${"complaintCode"}}`, encodeURIComponent(String(complaintCode)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            setSearchParams(localVarUrlObj, localVarQueryParameter, options.query);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 
         * @param {string} complaintId 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        complaintById: async (complaintId: string, options: any = {}): Promise<RequestArgs> => {
            // verify required parameter 'complaintId' is not null or undefined
            assertParamExists('complaintById', 'complaintId', complaintId)
            const localVarPath = `/complaint/{complaintId}`
                .replace(`{${"complaintId"}}`, encodeURIComponent(String(complaintId)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            setSearchParams(localVarUrlObj, localVarQueryParameter, options.query);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 
         * @param {Array<ComplaintId>} complaintId 
         * @param {number} [page] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        complaintsByIds: async (complaintId: Array<ComplaintId>, page?: number, options: any = {}): Promise<RequestArgs> => {
            // verify required parameter 'complaintId' is not null or undefined
            assertParamExists('complaintsByIds', 'complaintId', complaintId)
            const localVarPath = `/complaint/byIds`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            if (complaintId) {
                localVarQueryParameter['complaintId'] = complaintId;
            }

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
        /**
         * 
         * @param {CreateComplaintRequest} createComplaintRequest 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        createComplaint: async (createComplaintRequest: CreateComplaintRequest, options: any = {}): Promise<RequestArgs> => {
            // verify required parameter 'createComplaintRequest' is not null or undefined
            assertParamExists('createComplaint', 'createComplaintRequest', createComplaintRequest)
            const localVarPath = `/complaint`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'POST', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            localVarHeaderParameter['Content-Type'] = 'application/json';

            setSearchParams(localVarUrlObj, localVarQueryParameter, options.query);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};
            localVarRequestOptions.data = serializeDataIfNeeded(createComplaintRequest, localVarRequestOptions, configuration)

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 
         * @param {number} [page] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        rejectedComplaints: async (page?: number, options: any = {}): Promise<RequestArgs> => {
            const localVarPath = `/complaint/rejected`;
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
        /**
         * 
         * @param {string} complaintId 
         * @param {UpdateComplaintRequest} updateComplaintRequest 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        updateComplaint: async (complaintId: string, updateComplaintRequest: UpdateComplaintRequest, options: any = {}): Promise<RequestArgs> => {
            // verify required parameter 'complaintId' is not null or undefined
            assertParamExists('updateComplaint', 'complaintId', complaintId)
            // verify required parameter 'updateComplaintRequest' is not null or undefined
            assertParamExists('updateComplaint', 'updateComplaintRequest', updateComplaintRequest)
            const localVarPath = `/complaint/{complaintId}`
                .replace(`{${"complaintId"}}`, encodeURIComponent(String(complaintId)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'PUT', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            localVarHeaderParameter['Content-Type'] = 'application/json';

            setSearchParams(localVarUrlObj, localVarQueryParameter, options.query);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};
            localVarRequestOptions.data = serializeDataIfNeeded(updateComplaintRequest, localVarRequestOptions, configuration)

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
    }
};

/**
 * ComplaintRestServiceApi - functional programming interface
 * @export
 */
export const ComplaintRestServiceApiFp = function(configuration?: Configuration) {
    const localVarAxiosParamCreator = ComplaintRestServiceApiAxiosParamCreator(configuration)
    return {
        /**
         * 
         * @param {number} [page] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async allComplaints(page?: number, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<PageDtoComplaintComplaintDto>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.allComplaints(page, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 
         * @param {string} complaintCode 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async complaintByCode(complaintCode: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<ComplaintDto>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.complaintByCode(complaintCode, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 
         * @param {string} complaintId 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async complaintById(complaintId: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<ComplaintDto>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.complaintById(complaintId, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 
         * @param {Array<ComplaintId>} complaintId 
         * @param {number} [page] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async complaintsByIds(complaintId: Array<ComplaintId>, page?: number, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<PageDtoComplaintComplaintDto>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.complaintsByIds(complaintId, page, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 
         * @param {CreateComplaintRequest} createComplaintRequest 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async createComplaint(createComplaintRequest: CreateComplaintRequest, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<ComplaintDto>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.createComplaint(createComplaintRequest, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 
         * @param {number} [page] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async rejectedComplaints(page?: number, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<PageDtoComplaintComplaintDto>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.rejectedComplaints(page, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 
         * @param {string} complaintId 
         * @param {UpdateComplaintRequest} updateComplaintRequest 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async updateComplaint(complaintId: string, updateComplaintRequest: UpdateComplaintRequest, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<string>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.updateComplaint(complaintId, updateComplaintRequest, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
    }
};

/**
 * ComplaintRestServiceApi - factory interface
 * @export
 */
export const ComplaintRestServiceApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    const localVarFp = ComplaintRestServiceApiFp(configuration)
    return {
        /**
         * 
         * @param {number} [page] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        allComplaints(page?: number, options?: any): AxiosPromise<PageDtoComplaintComplaintDto> {
            return localVarFp.allComplaints(page, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @param {string} complaintCode 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        complaintByCode(complaintCode: string, options?: any): AxiosPromise<ComplaintDto> {
            return localVarFp.complaintByCode(complaintCode, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @param {string} complaintId 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        complaintById(complaintId: string, options?: any): AxiosPromise<ComplaintDto> {
            return localVarFp.complaintById(complaintId, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @param {Array<ComplaintId>} complaintId 
         * @param {number} [page] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        complaintsByIds(complaintId: Array<ComplaintId>, page?: number, options?: any): AxiosPromise<PageDtoComplaintComplaintDto> {
            return localVarFp.complaintsByIds(complaintId, page, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @param {CreateComplaintRequest} createComplaintRequest 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        createComplaint(createComplaintRequest: CreateComplaintRequest, options?: any): AxiosPromise<ComplaintDto> {
            return localVarFp.createComplaint(createComplaintRequest, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @param {number} [page] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        rejectedComplaints(page?: number, options?: any): AxiosPromise<PageDtoComplaintComplaintDto> {
            return localVarFp.rejectedComplaints(page, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @param {string} complaintId 
         * @param {UpdateComplaintRequest} updateComplaintRequest 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        updateComplaint(complaintId: string, updateComplaintRequest: UpdateComplaintRequest, options?: any): AxiosPromise<string> {
            return localVarFp.updateComplaint(complaintId, updateComplaintRequest, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * ComplaintRestServiceApi - interface
 * @export
 * @interface ComplaintRestServiceApi
 */
export interface ComplaintRestServiceApiInterface {
    /**
     * 
     * @param {number} [page] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ComplaintRestServiceApiInterface
     */
    allComplaints(page?: number, options?: any): AxiosPromise<PageDtoComplaintComplaintDto>;

    /**
     * 
     * @param {string} complaintCode 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ComplaintRestServiceApiInterface
     */
    complaintByCode(complaintCode: string, options?: any): AxiosPromise<ComplaintDto>;

    /**
     * 
     * @param {string} complaintId 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ComplaintRestServiceApiInterface
     */
    complaintById(complaintId: string, options?: any): AxiosPromise<ComplaintDto>;

    /**
     * 
     * @param {Array<ComplaintId>} complaintId 
     * @param {number} [page] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ComplaintRestServiceApiInterface
     */
    complaintsByIds(complaintId: Array<ComplaintId>, page?: number, options?: any): AxiosPromise<PageDtoComplaintComplaintDto>;

    /**
     * 
     * @param {CreateComplaintRequest} createComplaintRequest 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ComplaintRestServiceApiInterface
     */
    createComplaint(createComplaintRequest: CreateComplaintRequest, options?: any): AxiosPromise<ComplaintDto>;

    /**
     * 
     * @param {number} [page] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ComplaintRestServiceApiInterface
     */
    rejectedComplaints(page?: number, options?: any): AxiosPromise<PageDtoComplaintComplaintDto>;

    /**
     * 
     * @param {string} complaintId 
     * @param {UpdateComplaintRequest} updateComplaintRequest 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ComplaintRestServiceApiInterface
     */
    updateComplaint(complaintId: string, updateComplaintRequest: UpdateComplaintRequest, options?: any): AxiosPromise<string>;

}

/**
 * ComplaintRestServiceApi - object-oriented interface
 * @export
 * @class ComplaintRestServiceApi
 * @extends {BaseAPI}
 */
export class ComplaintRestServiceApi extends BaseAPI implements ComplaintRestServiceApiInterface {
    /**
     * 
     * @param {number} [page] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ComplaintRestServiceApi
     */
    public allComplaints(page?: number, options?: any) {
        return ComplaintRestServiceApiFp(this.configuration).allComplaints(page, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @param {string} complaintCode 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ComplaintRestServiceApi
     */
    public complaintByCode(complaintCode: string, options?: any) {
        return ComplaintRestServiceApiFp(this.configuration).complaintByCode(complaintCode, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @param {string} complaintId 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ComplaintRestServiceApi
     */
    public complaintById(complaintId: string, options?: any) {
        return ComplaintRestServiceApiFp(this.configuration).complaintById(complaintId, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @param {Array<ComplaintId>} complaintId 
     * @param {number} [page] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ComplaintRestServiceApi
     */
    public complaintsByIds(complaintId: Array<ComplaintId>, page?: number, options?: any) {
        return ComplaintRestServiceApiFp(this.configuration).complaintsByIds(complaintId, page, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @param {CreateComplaintRequest} createComplaintRequest 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ComplaintRestServiceApi
     */
    public createComplaint(createComplaintRequest: CreateComplaintRequest, options?: any) {
        return ComplaintRestServiceApiFp(this.configuration).createComplaint(createComplaintRequest, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @param {number} [page] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ComplaintRestServiceApi
     */
    public rejectedComplaints(page?: number, options?: any) {
        return ComplaintRestServiceApiFp(this.configuration).rejectedComplaints(page, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @param {string} complaintId 
     * @param {UpdateComplaintRequest} updateComplaintRequest 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ComplaintRestServiceApi
     */
    public updateComplaint(complaintId: string, updateComplaintRequest: UpdateComplaintRequest, options?: any) {
        return ComplaintRestServiceApiFp(this.configuration).updateComplaint(complaintId, updateComplaintRequest, options).then((request) => request(this.axios, this.basePath));
    }
}
