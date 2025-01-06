import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDomain } from '../domain.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../domain.test-samples';

import { DomainService } from './domain.service';

const requireRestSample: IDomain = {
  ...sampleWithRequiredData,
};

describe('Domain Service', () => {
  let service: DomainService;
  let httpMock: HttpTestingController;
  let expectedResult: IDomain | IDomain[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DomainService);
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

    it('should create a Domain', () => {
      const domain = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(domain).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Domain', () => {
      const domain = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(domain).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Domain', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Domain', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Domain', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDomainToCollectionIfMissing', () => {
      it('should add a Domain to an empty array', () => {
        const domain: IDomain = sampleWithRequiredData;
        expectedResult = service.addDomainToCollectionIfMissing([], domain);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(domain);
      });

      it('should not add a Domain to an array that contains it', () => {
        const domain: IDomain = sampleWithRequiredData;
        const domainCollection: IDomain[] = [
          {
            ...domain,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDomainToCollectionIfMissing(domainCollection, domain);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Domain to an array that doesn't contain it", () => {
        const domain: IDomain = sampleWithRequiredData;
        const domainCollection: IDomain[] = [sampleWithPartialData];
        expectedResult = service.addDomainToCollectionIfMissing(domainCollection, domain);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(domain);
      });

      it('should add only unique Domain to an array', () => {
        const domainArray: IDomain[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const domainCollection: IDomain[] = [sampleWithRequiredData];
        expectedResult = service.addDomainToCollectionIfMissing(domainCollection, ...domainArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const domain: IDomain = sampleWithRequiredData;
        const domain2: IDomain = sampleWithPartialData;
        expectedResult = service.addDomainToCollectionIfMissing([], domain, domain2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(domain);
        expect(expectedResult).toContain(domain2);
      });

      it('should accept null and undefined values', () => {
        const domain: IDomain = sampleWithRequiredData;
        expectedResult = service.addDomainToCollectionIfMissing([], null, domain, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(domain);
      });

      it('should return initial array if no Domain is added', () => {
        const domainCollection: IDomain[] = [sampleWithRequiredData];
        expectedResult = service.addDomainToCollectionIfMissing(domainCollection, undefined, null);
        expect(expectedResult).toEqual(domainCollection);
      });
    });

    describe('compareDomain', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDomain(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDomain(entity1, entity2);
        const compareResult2 = service.compareDomain(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDomain(entity1, entity2);
        const compareResult2 = service.compareDomain(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDomain(entity1, entity2);
        const compareResult2 = service.compareDomain(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
