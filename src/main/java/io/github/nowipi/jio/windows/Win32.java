package io.github.nowipi.jio.windows;

import java.lang.foreign.*;

import static java.lang.foreign.ValueLayout.*;

public final class Win32 {

    public static final Arena arena = Arena.global();
    // Primitive base aliases
    public static final OfByte   BYTE     = JAVA_BYTE;
    public static final OfShort  SHORT    = JAVA_SHORT;
    public static final OfInt    INT      = JAVA_INT;
    public static final OfLong   LONG     = JAVA_LONG;
    public static final OfFloat  FLOAT    = JAVA_FLOAT;
    public static final OfDouble DOUBLE   = JAVA_DOUBLE;
    public static final AddressLayout        ADDRESS  = ValueLayout.ADDRESS;

    // Character types
    public static final OfByte CHAR      = JAVA_BYTE.withName("CHAR");       // 1 byte
    public static final OfShort WCHAR     = JAVA_SHORT.withName("WCHAR");     // 2 bytes (UTF-16 LE)

    // Boolean
    public static final OfInt BOOL      = JAVA_INT.withName("BOOL");        // 4 bytes

    // Integer types
    public static final OfByte BYTE_T    = JAVA_BYTE.withName("BYTE");       // 1 byte unsigned
    public static final OfShort SHORT_T   = JAVA_SHORT.withName("SHORT");     // 2 bytes signed
    public static final OfShort USHORT    = JAVA_SHORT.withName("USHORT");    // 2 bytes unsigned
    public static final OfShort WORD      = JAVA_SHORT.withName("WORD");      // 2 bytes unsigned

    public static final OfInt INT_T     = JAVA_INT.withName("INT");         // 4 bytes signed
    public static final OfInt UINT      = JAVA_INT.withName("UINT");        // 4 bytes unsigned
    public static final OfInt LONG_T    = JAVA_INT.withName("LONG");        // 4 bytes signed (WinAPI defines LONG as 32-bit)
    public static final OfInt ULONG     = JAVA_INT.withName("ULONG");       // 4 bytes unsigned
    public static final OfInt DWORD     = JAVA_INT.withName("DWORD");       // 4 bytes unsigned
    public static final OfInt DWORD32   = JAVA_INT.withName("DWORD32");     // 4 bytes unsigned
    public static final OfLong QWORD     = JAVA_LONG.withName("QWORD");      // 8 bytes unsigned
    public static final OfLong LONG64    = JAVA_LONG.withName("LONG64");     // 8 bytes signed
    public static final OfLong ULONG64   = JAVA_LONG.withName("ULONG64");    // 8 bytes unsigned

    // Floating-point
    public static final OfFloat FLOAT_T   = JAVA_FLOAT.withName("FLOAT");     // 4 bytes
    public static final OfDouble DOUBLE_T  = JAVA_DOUBLE.withName("DOUBLE");   // 8 bytes

    // Pointer/handle types (64-bit pointer size)
    public static final AddressLayout HANDLE    = ADDRESS.withName("HANDLE");
    public static final AddressLayout HWND      = ADDRESS.withName("HWND");
    public static final AddressLayout HINSTANCE = ADDRESS.withName("HINSTANCE");
    public static final AddressLayout HMODULE   = ADDRESS.withName("HMODULE");
    public static final AddressLayout HDC       = ADDRESS.withName("HDC");
    public static final AddressLayout HMENU     = ADDRESS.withName("HMENU");
    public static final AddressLayout HCURSOR   = ADDRESS.withName("HCURSOR");
    public static final AddressLayout HICON     = ADDRESS.withName("HICON");
    public static final AddressLayout HBRUSH    = ADDRESS.withName("HBRUSH");
    public static final AddressLayout HFONT     = ADDRESS.withName("HFONT");
    public static final AddressLayout HBITMAP   = ADDRESS.withName("HBITMAP");

    // String/Pointer aliases
    public static final AddressLayout LPSTR     = ADDRESS.withName("LPSTR");   // Pointer to CHAR
    public static final AddressLayout LPCSTR    = ADDRESS.withName("LPCSTR");  // Pointer to const CHAR
    public static final AddressLayout LPWSTR    = ADDRESS.withName("LPWSTR");  // Pointer to WCHAR
    public static final AddressLayout LPCWSTR   = ADDRESS.withName("LPCWSTR"); // Pointer to const WCHAR
    public static final AddressLayout LPVOID    = ADDRESS.withName("LPVOID");  // Pointer to void
    public static final AddressLayout PVOID     = ADDRESS.withName("PVOID");   // Pointer to void
    public static final AddressLayout LRESULT   = ADDRESS.withName("LRESULT"); // Pointer-sized return type

    // Size-specific
    public static final OfLong SIZE_T    = JAVA_LONG.withName("SIZE_T");     // 8 bytes on Win64
    public static final OfLong SSIZE_T   = JAVA_LONG.withName("SSIZE_T");    // 8 bytes signed
    public static final OfLong INT_PTR   = JAVA_LONG.withName("INT_PTR");    // signed pointer-sized
    public static final OfLong UINT_PTR  = JAVA_LONG.withName("UINT_PTR");   // unsigned pointer-sized
    public static final OfLong ULONG_PTR = JAVA_LONG.withName("ULONG_PTR");  // unsigned pointer-sized
}
