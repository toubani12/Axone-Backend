import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWallet } from '../wallet.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../wallet.test-samples';

import { WalletService } from './wallet.service';

const requireRestSample: IWallet = {
  ...sampleWithRequiredData,
};

describe('Wallet Service', () => {
  let service: WalletService;
  let httpMock: HttpTestingController;
  let expectedResult: IWallet | IWallet[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WalletService);
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

    it('should create a Wallet', () => {
      const wallet = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(wallet).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Wallet', () => {
      const wallet = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(wallet).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Wallet', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Wallet', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Wallet', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWalletToCollectionIfMissing', () => {
      it('should add a Wallet to an empty array', () => {
        const wallet: IWallet = sampleWithRequiredData;
        expectedResult = service.addWalletToCollectionIfMissing([], wallet);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wallet);
      });

      it('should not add a Wallet to an array that contains it', () => {
        const wallet: IWallet = sampleWithRequiredData;
        const walletCollection: IWallet[] = [
          {
            ...wallet,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWalletToCollectionIfMissing(walletCollection, wallet);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Wallet to an array that doesn't contain it", () => {
        const wallet: IWallet = sampleWithRequiredData;
        const walletCollection: IWallet[] = [sampleWithPartialData];
        expectedResult = service.addWalletToCollectionIfMissing(walletCollection, wallet);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wallet);
      });

      it('should add only unique Wallet to an array', () => {
        const walletArray: IWallet[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const walletCollection: IWallet[] = [sampleWithRequiredData];
        expectedResult = service.addWalletToCollectionIfMissing(walletCollection, ...walletArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const wallet: IWallet = sampleWithRequiredData;
        const wallet2: IWallet = sampleWithPartialData;
        expectedResult = service.addWalletToCollectionIfMissing([], wallet, wallet2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wallet);
        expect(expectedResult).toContain(wallet2);
      });

      it('should accept null and undefined values', () => {
        const wallet: IWallet = sampleWithRequiredData;
        expectedResult = service.addWalletToCollectionIfMissing([], null, wallet, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wallet);
      });

      it('should return initial array if no Wallet is added', () => {
        const walletCollection: IWallet[] = [sampleWithRequiredData];
        expectedResult = service.addWalletToCollectionIfMissing(walletCollection, undefined, null);
        expect(expectedResult).toEqual(walletCollection);
      });
    });

    describe('compareWallet', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWallet(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareWallet(entity1, entity2);
        const compareResult2 = service.compareWallet(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareWallet(entity1, entity2);
        const compareResult2 = service.compareWallet(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareWallet(entity1, entity2);
        const compareResult2 = service.compareWallet(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
