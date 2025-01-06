import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICriteria } from '../criteria.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../criteria.test-samples';

import { CriteriaService } from './criteria.service';

const requireRestSample: ICriteria = {
  ...sampleWithRequiredData,
};

describe('Criteria Service', () => {
  let service: CriteriaService;
  let httpMock: HttpTestingController;
  let expectedResult: ICriteria | ICriteria[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CriteriaService);
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

    it('should create a Criteria', () => {
      const criteria = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(criteria).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Criteria', () => {
      const criteria = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(criteria).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Criteria', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Criteria', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Criteria', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCriteriaToCollectionIfMissing', () => {
      it('should add a Criteria to an empty array', () => {
        const criteria: ICriteria = sampleWithRequiredData;
        expectedResult = service.addCriteriaToCollectionIfMissing([], criteria);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(criteria);
      });

      it('should not add a Criteria to an array that contains it', () => {
        const criteria: ICriteria = sampleWithRequiredData;
        const criteriaCollection: ICriteria[] = [
          {
            ...criteria,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCriteriaToCollectionIfMissing(criteriaCollection, criteria);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Criteria to an array that doesn't contain it", () => {
        const criteria: ICriteria = sampleWithRequiredData;
        const criteriaCollection: ICriteria[] = [sampleWithPartialData];
        expectedResult = service.addCriteriaToCollectionIfMissing(criteriaCollection, criteria);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(criteria);
      });

      it('should add only unique Criteria to an array', () => {
        const criteriaArray: ICriteria[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const criteriaCollection: ICriteria[] = [sampleWithRequiredData];
        expectedResult = service.addCriteriaToCollectionIfMissing(criteriaCollection, ...criteriaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const criteria: ICriteria = sampleWithRequiredData;
        const criteria2: ICriteria = sampleWithPartialData;
        expectedResult = service.addCriteriaToCollectionIfMissing([], criteria, criteria2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(criteria);
        expect(expectedResult).toContain(criteria2);
      });

      it('should accept null and undefined values', () => {
        const criteria: ICriteria = sampleWithRequiredData;
        expectedResult = service.addCriteriaToCollectionIfMissing([], null, criteria, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(criteria);
      });

      it('should return initial array if no Criteria is added', () => {
        const criteriaCollection: ICriteria[] = [sampleWithRequiredData];
        expectedResult = service.addCriteriaToCollectionIfMissing(criteriaCollection, undefined, null);
        expect(expectedResult).toEqual(criteriaCollection);
      });
    });

    describe('compareCriteria', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCriteria(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCriteria(entity1, entity2);
        const compareResult2 = service.compareCriteria(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCriteria(entity1, entity2);
        const compareResult2 = service.compareCriteria(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCriteria(entity1, entity2);
        const compareResult2 = service.compareCriteria(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
