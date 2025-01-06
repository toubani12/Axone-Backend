import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProvider } from '../provider.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../provider.test-samples';

import { ProviderService } from './provider.service';

const requireRestSample: IProvider = {
  ...sampleWithRequiredData,
};

describe('Provider Service', () => {
  let service: ProviderService;
  let httpMock: HttpTestingController;
  let expectedResult: IProvider | IProvider[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProviderService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Provider', () => {
      const provider = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(provider).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Provider', () => {
      const provider = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(provider).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Provider', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Provider', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Provider', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProviderToCollectionIfMissing', () => {
      it('should add a Provider to an empty array', () => {
        const provider: IProvider = sampleWithRequiredData;
        expectedResult = service.addProviderToCollectionIfMissing([], provider);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(provider);
      });

      it('should not add a Provider to an array that contains it', () => {
        const provider: IProvider = sampleWithRequiredData;
        const providerCollection: IProvider[] = [
          {
            ...provider,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProviderToCollectionIfMissing(providerCollection, provider);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Provider to an array that doesn't contain it", () => {
        const provider: IProvider = sampleWithRequiredData;
        const providerCollection: IProvider[] = [sampleWithPartialData];
        expectedResult = service.addProviderToCollectionIfMissing(providerCollection, provider);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(provider);
      });

      it('should add only unique Provider to an array', () => {
        const providerArray: IProvider[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const providerCollection: IProvider[] = [sampleWithRequiredData];
        expectedResult = service.addProviderToCollectionIfMissing(providerCollection, ...providerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const provider: IProvider = sampleWithRequiredData;
        const provider2: IProvider = sampleWithPartialData;
        expectedResult = service.addProviderToCollectionIfMissing([], provider, provider2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(provider);
        expect(expectedResult).toContain(provider2);
      });

      it('should accept null and undefined values', () => {
        const provider: IProvider = sampleWithRequiredData;
        expectedResult = service.addProviderToCollectionIfMissing([], null, provider, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(provider);
      });

      it('should return initial array if no Provider is added', () => {
        const providerCollection: IProvider[] = [sampleWithRequiredData];
        expectedResult = service.addProviderToCollectionIfMissing(providerCollection, undefined, null);
        expect(expectedResult).toEqual(providerCollection);
      });
    });

    describe('compareProvider', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProvider(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProvider(entity1, entity2);
        const compareResult2 = service.compareProvider(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProvider(entity1, entity2);
        const compareResult2 = service.compareProvider(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProvider(entity1, entity2);
        const compareResult2 = service.compareProvider(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
