import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { INDA } from '../nda.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../nda.test-samples';

import { NDAService, RestNDA } from './nda.service';

const requireRestSample: RestNDA = {
  ...sampleWithRequiredData,
  period: sampleWithRequiredData.period?.format(DATE_FORMAT),
};

describe('NDA Service', () => {
  let service: NDAService;
  let httpMock: HttpTestingController;
  let expectedResult: INDA | INDA[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NDAService);
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

    it('should create a NDA', () => {
      const nDA = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(nDA).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NDA', () => {
      const nDA = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(nDA).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NDA', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NDA', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a NDA', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addNDAToCollectionIfMissing', () => {
      it('should add a NDA to an empty array', () => {
        const nDA: INDA = sampleWithRequiredData;
        expectedResult = service.addNDAToCollectionIfMissing([], nDA);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nDA);
      });

      it('should not add a NDA to an array that contains it', () => {
        const nDA: INDA = sampleWithRequiredData;
        const nDACollection: INDA[] = [
          {
            ...nDA,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addNDAToCollectionIfMissing(nDACollection, nDA);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NDA to an array that doesn't contain it", () => {
        const nDA: INDA = sampleWithRequiredData;
        const nDACollection: INDA[] = [sampleWithPartialData];
        expectedResult = service.addNDAToCollectionIfMissing(nDACollection, nDA);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nDA);
      });

      it('should add only unique NDA to an array', () => {
        const nDAArray: INDA[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const nDACollection: INDA[] = [sampleWithRequiredData];
        expectedResult = service.addNDAToCollectionIfMissing(nDACollection, ...nDAArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const nDA: INDA = sampleWithRequiredData;
        const nDA2: INDA = sampleWithPartialData;
        expectedResult = service.addNDAToCollectionIfMissing([], nDA, nDA2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nDA);
        expect(expectedResult).toContain(nDA2);
      });

      it('should accept null and undefined values', () => {
        const nDA: INDA = sampleWithRequiredData;
        expectedResult = service.addNDAToCollectionIfMissing([], null, nDA, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nDA);
      });

      it('should return initial array if no NDA is added', () => {
        const nDACollection: INDA[] = [sampleWithRequiredData];
        expectedResult = service.addNDAToCollectionIfMissing(nDACollection, undefined, null);
        expect(expectedResult).toEqual(nDACollection);
      });
    });

    describe('compareNDA', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareNDA(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareNDA(entity1, entity2);
        const compareResult2 = service.compareNDA(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareNDA(entity1, entity2);
        const compareResult2 = service.compareNDA(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareNDA(entity1, entity2);
        const compareResult2 = service.compareNDA(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
