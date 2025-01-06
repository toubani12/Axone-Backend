import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContractType } from '../contract-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../contract-type.test-samples';

import { ContractTypeService } from './contract-type.service';

const requireRestSample: IContractType = {
  ...sampleWithRequiredData,
};

describe('ContractType Service', () => {
  let service: ContractTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IContractType | IContractType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContractTypeService);
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

    it('should create a ContractType', () => {
      const contractType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contractType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContractType', () => {
      const contractType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contractType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ContractType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContractType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ContractType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addContractTypeToCollectionIfMissing', () => {
      it('should add a ContractType to an empty array', () => {
        const contractType: IContractType = sampleWithRequiredData;
        expectedResult = service.addContractTypeToCollectionIfMissing([], contractType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contractType);
      });

      it('should not add a ContractType to an array that contains it', () => {
        const contractType: IContractType = sampleWithRequiredData;
        const contractTypeCollection: IContractType[] = [
          {
            ...contractType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContractTypeToCollectionIfMissing(contractTypeCollection, contractType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContractType to an array that doesn't contain it", () => {
        const contractType: IContractType = sampleWithRequiredData;
        const contractTypeCollection: IContractType[] = [sampleWithPartialData];
        expectedResult = service.addContractTypeToCollectionIfMissing(contractTypeCollection, contractType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contractType);
      });

      it('should add only unique ContractType to an array', () => {
        const contractTypeArray: IContractType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contractTypeCollection: IContractType[] = [sampleWithRequiredData];
        expectedResult = service.addContractTypeToCollectionIfMissing(contractTypeCollection, ...contractTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contractType: IContractType = sampleWithRequiredData;
        const contractType2: IContractType = sampleWithPartialData;
        expectedResult = service.addContractTypeToCollectionIfMissing([], contractType, contractType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contractType);
        expect(expectedResult).toContain(contractType2);
      });

      it('should accept null and undefined values', () => {
        const contractType: IContractType = sampleWithRequiredData;
        expectedResult = service.addContractTypeToCollectionIfMissing([], null, contractType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contractType);
      });

      it('should return initial array if no ContractType is added', () => {
        const contractTypeCollection: IContractType[] = [sampleWithRequiredData];
        expectedResult = service.addContractTypeToCollectionIfMissing(contractTypeCollection, undefined, null);
        expect(expectedResult).toEqual(contractTypeCollection);
      });
    });

    describe('compareContractType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContractType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareContractType(entity1, entity2);
        const compareResult2 = service.compareContractType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareContractType(entity1, entity2);
        const compareResult2 = service.compareContractType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareContractType(entity1, entity2);
        const compareResult2 = service.compareContractType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
