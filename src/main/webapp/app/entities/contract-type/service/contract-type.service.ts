import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContractType, NewContractType } from '../contract-type.model';

export type PartialUpdateContractType = Partial<IContractType> & Pick<IContractType, 'id'>;

export type EntityResponseType = HttpResponse<IContractType>;
export type EntityArrayResponseType = HttpResponse<IContractType[]>;

@Injectable({ providedIn: 'root' })
export class ContractTypeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contract-types');

  create(contractType: NewContractType): Observable<EntityResponseType> {
    return this.http.post<IContractType>(this.resourceUrl, contractType, { observe: 'response' });
  }

  update(contractType: IContractType): Observable<EntityResponseType> {
    return this.http.put<IContractType>(`${this.resourceUrl}/${this.getContractTypeIdentifier(contractType)}`, contractType, {
      observe: 'response',
    });
  }

  partialUpdate(contractType: PartialUpdateContractType): Observable<EntityResponseType> {
    return this.http.patch<IContractType>(`${this.resourceUrl}/${this.getContractTypeIdentifier(contractType)}`, contractType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContractType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContractType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getContractTypeIdentifier(contractType: Pick<IContractType, 'id'>): number {
    return contractType.id;
  }

  compareContractType(o1: Pick<IContractType, 'id'> | null, o2: Pick<IContractType, 'id'> | null): boolean {
    return o1 && o2 ? this.getContractTypeIdentifier(o1) === this.getContractTypeIdentifier(o2) : o1 === o2;
  }

  addContractTypeToCollectionIfMissing<Type extends Pick<IContractType, 'id'>>(
    contractTypeCollection: Type[],
    ...contractTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contractTypes: Type[] = contractTypesToCheck.filter(isPresent);
    if (contractTypes.length > 0) {
      const contractTypeCollectionIdentifiers = contractTypeCollection.map(contractTypeItem =>
        this.getContractTypeIdentifier(contractTypeItem),
      );
      const contractTypesToAdd = contractTypes.filter(contractTypeItem => {
        const contractTypeIdentifier = this.getContractTypeIdentifier(contractTypeItem);
        if (contractTypeCollectionIdentifiers.includes(contractTypeIdentifier)) {
          return false;
        }
        contractTypeCollectionIdentifiers.push(contractTypeIdentifier);
        return true;
      });
      return [...contractTypesToAdd, ...contractTypeCollection];
    }
    return contractTypeCollection;
  }
}
