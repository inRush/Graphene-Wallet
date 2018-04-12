package cy.agorise.graphenej;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The price struct stores asset prices in the Graphene system.
 *
 * A price is defined as a ratio between two assets, and represents a possible exchange rate
 * between those two assets. prices are generally not stored in any simplified form, i.e. a price
 * of (1000 CORE)/(20 USD) is perfectly normal.

 * The assets within a price are labeled base and quote. Throughout the Graphene code base,
 * the convention used is that the base asset is the asset being sold, and the quote asset is
 * the asset being purchased, where the price is represented as base/quote, so in the example
 * price above the seller is looking to sell CORE asset and get USD in return.
 *
 * Note: Taken from the Graphene doxygen.
 *
 * @author nelson
 * @date 12/16/16
 */
public class Price implements Parcelable {
    public AssetAmount base;
    public AssetAmount quote;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.base);
        dest.writeSerializable(this.quote);
    }

    public Price() {
    }

    protected Price(Parcel in) {
        this.base = (AssetAmount) in.readSerializable();
        this.quote = (AssetAmount) in.readSerializable();
    }

    public static final Parcelable.Creator<Price> CREATOR = new Parcelable.Creator<Price>() {
        @Override
        public Price createFromParcel(Parcel source) {
            return new Price(source);
        }

        @Override
        public Price[] newArray(int size) {
            return new Price[size];
        }
    };
}
