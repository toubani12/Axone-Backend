import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITechnicalCV } from '../technical-cv.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../technical-cv.test-samples';

import { TechnicalCVService } from './technical-cv.service';

const requireRestSample: ITechnicalCV = {
  ...sampleWithRequiredData,
};

describe('TechnicalCV Service', () => {
  let service: TechnicalCVService;
  let httpMock: HttpTestingController;
  let expectedResult: ITechnicalCV | ITechnicalCV[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TechnicalCVService);
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

    it('should create a TechnicalCV', () => {
      const technicalCV = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(technicalCV).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TechnicalCV', () => {
      const technicalCV = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(technicalCV).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TechnicalCV', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TechnicalCV', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TechnicalCV', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTechnicalCVToCollectionIfMissing', () => {
      it('should add a TechnicalCV to an empty array', () => {
        const technicalCV: ITechnicalCV = sampleWithRequiredData;
        expectedResult = service.addTechnicalCVToCollectionIfMissing([], technicalCV);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(technicalCV);
      });

      it('should not add a TechnicalCV to an array that contains it', () => {
        const technicalCV: ITechnicalCV = sampleWithRequiredData;
        const technicalCVCollection: ITechnicalCV[] = [
          {
            ...technicalCV,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTechnicalCVToCollectionIfMissing(technicalCVCollection, technicalCV);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TechnicalCV to an array that doesn't contain it", () => {
        const technicalCV: ITechnicalCV = sampleWithRequiredData;
        const technicalCVCollection: ITechnicalCV[] = [sampleWithPartialData];
        expectedResult = service.addTechnicalCVToCollectionIfMissing(technicalCVCollection, technicalCV);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(technicalCV);
      });

      it('should add only unique TechnicalCV to an array', () => {
        const technicalCVArray: ITechnicalCV[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const technicalCVCollection: ITechnicalCV[] = [sampleWithRequiredData];
        expectedResult = service.addTechnicalCVToCollectionIfMissing(technicalCVCollection, ...technicalCVArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const technicalCV: ITechnicalCV = sampleWithRequiredData;
        const technicalCV2: ITechnicalCV = sampleWithPartialData;
        expectedResult = service.addTechnicalCVToCollectionIfMissing([], technicalCV, technicalCV2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(technicalCV);
        expect(expectedResult).toContain(technicalCV2);
      });

      it('should accept null and undefined values', () => {
        const technicalCV: ITechnicalCV = sampleWithRequiredData;
        expectedResult = service.addTechnicalCVToCollectionIfMissing([], null, technicalCV, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(technicalCV);
      });

      it('should return initial array if no TechnicalCV is added', () => {
        const technicalCVCollection: ITechnicalCV[] = [sampleWithRequiredData];
        expectedResult = service.addTechnicalCVToCollectionIfMissing(technicalCVCollection, undefined, null);
        expect(expectedResult).toEqual(technicalCVCollection);
      });
    });

    describe('compareTechnicalCV', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTechnicalCV(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTechnicalCV(entity1, entity2);
        const compareResult2 = service.compareTechnicalCV(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTechnicalCV(entity1, entity2);
        const compareResult2 = service.compareTechnicalCV(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTechnicalCV(entity1, entity2);
        const compareResult2 = service.compareTechnicalCV(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
