import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProvider, NewProvider } from '../provider.model';

export type PartialUpdateProvider = Partial<IProvider> & Pick<IProvider, 'id'>;

export type EntityResponseType = HttpResponse<IProvider>;
export type EntityArrayResponseType = HttpResponse<IProvider[]>;

@Injectable({ providedIn: 'root' })
export class ProviderService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/providers');

  create(provider: NewProvider): Observable<EntityResponseType> {
    return this.http.post<IProvider>(this.resourceUrl, provider, { observe: 'response' });
  }

  update(provider: IProvider): Observable<EntityResponseType> {
    return this.http.put<IProvider>(`${this.resourceUrl}/${this.getProviderIdentifier(provider)}`, provider, { observe: 'response' });
  }

  partialUpdate(provider: PartialUpdateProvider): Observable<EntityResponseType> {
    return this.http.patch<IProvider>(`${this.resourceUrl}/${this.getProviderIdentifier(provider)}`, provider, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProvider>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProvider[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProviderIdentifier(provider: Pick<IProvider, 'id'>): number {
    return provider.id;
  }

  compareProvider(o1: Pick<IProvider, 'id'> | null, o2: Pick<IProvider, 'id'> | null): boolean {
    return o1 && o2 ? this.getProviderIdentifier(o1) === this.getProviderIdentifier(o2) : o1 === o2;
  }

  addProviderToCollectionIfMissing<Type extends Pick<IProvider, 'id'>>(
    providerCollection: Type[],
    ...providersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const providers: Type[] = providersToCheck.filter(isPresent);
    if (providers.length > 0) {
      const providerCollectionIdentifiers = providerCollection.map(providerItem => this.getProviderIdentifier(providerItem));
      const providersToAdd = providers.filter(providerItem => {
        const providerIdentifier = this.getProviderIdentifier(providerItem);
        if (providerCollectionIdentifiers.includes(providerIdentifier)) {
          return false;
        }
        providerCollectionIdentifiers.push(providerIdentifier);
        return true;
      });
      return [...providersToAdd, ...providerCollection];
    }
    return providerCollection;
  }
}
